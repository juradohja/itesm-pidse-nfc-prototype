package itesm.juradohja.nfcprototype;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

class NfcReader {

    // Total de campos que debe contener el tag
    private final int tagLength = 1;
    private Cipher cipher;
    private SecretKey key;
    private byte[] iv;

    NfcReader(Context ctx) throws NoSuchAlgorithmException, IOException, ClassNotFoundException, NoSuchPaddingException {
        AssetManager assetManager = ctx.getAssets();
        InputStream is = assetManager.open("secretKey.txt");
        ObjectInputStream ois = new ObjectInputStream(is);
        key = (SecretKey) ois.readObject();
        is.close();
        iv = new byte[16];
        InputStream is2 = assetManager.open("iv.txt");
        is2.read(iv);
        is2.close();
        cipher = Cipher.getInstance(key.getAlgorithm() + "/CBC/PKCS5Padding");
    }

    // LECTURA DE TAGS
    TagProfile readFromIntent(Intent intent) throws NotNfcActionException, EmptyTagException, ImproperTagException {
        try {
            String action = intent.getAction();
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                    || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                    || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                NdefMessage[] msgs;
                if (rawMsgs != null) {
                    msgs = new NdefMessage[rawMsgs.length];
                    for (int i = 0; i < rawMsgs.length; i++) {
                        msgs[i] = (NdefMessage) rawMsgs[i];
                    }
                    return buildProfile(msgs);
                } else throw new EmptyTagException();
            } else throw new NotNfcActionException();
        } catch (Exception ignored) {
            return new TagProfile();
        }
    }

    private TagProfile buildProfile(NdefMessage[] msgs) throws EmptyTagException, ImproperTagException {
        try {
            if (msgs == null || msgs.length == 0) throw new EmptyTagException();
            String[] records = new String[msgs[0].getRecords().length];
            if (records.length == tagLength) {
                return decryptProfile(msgs[0].getRecords()[0].getPayload());
            } else throw new ImproperTagException();
        } catch (ImproperTagException e) {
            // Toast for improper tag
            return new TagProfile();
        } catch (Exception e) {
            e.printStackTrace();
            return new TagProfile();
        }
    }

    private TagProfile decryptProfile(byte[] bytes) throws IOException, ClassNotFoundException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        SealedObject sealedObject = (SealedObject) ois.readObject();
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        return (TagProfile) sealedObject.getObject(cipher);
    }

    // ESCRITURA DE TAGS
    void writeToTag(TagProfile profile, Tag tag) throws IOException, FormatException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException {
        NdefRecord[] encryptedRecord = {createRecord(profile)};
        NdefMessage encryptedMessage = new NdefMessage(encryptedRecord);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(encryptedMessage);
        ndef.close();
    }

    private NdefRecord createRecord(TagProfile tagProfile) throws IOException, InvalidKeyException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] profileBytes = encryptProfile(tagProfile);
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], profileBytes);
    }

    private byte[] encryptProfile(TagProfile tagProfile) throws InvalidKeyException, IOException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        SealedObject sealedProfile = new SealedObject(tagProfile, cipher);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(sealedProfile);
        oos.flush();
        return bos.toByteArray();
    }

    private static class NotNfcActionException extends RuntimeException {
        NotNfcActionException() {
        }
    }

    private static class EmptyTagException extends RuntimeException {
        EmptyTagException() {
        }
    }

    private static class ImproperTagException extends RuntimeException {
        ImproperTagException() {
            System.out.println("Tag content does not match application requirements.");
        }
    }

}
