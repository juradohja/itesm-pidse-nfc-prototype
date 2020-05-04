package itesm.juradohja.nfcprototype;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public abstract class NfcActivity extends AppCompatActivity implements NfcWriteDialogFragment.NFCTagWriteDialogListener {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";

    protected Context context;
    protected NfcAdapter nfcAdapter;
    protected NfcReader nfcReader;
    protected TagProfile tagProfile;
    protected PendingIntent pendingIntent;
    protected IntentFilter[] writeTagFilters;
    protected Tag myTag;
    protected DialogFragment writeFragment;
    protected boolean isWriting;
    //   protected boolean writeMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        isWriting = false;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        try {
            nfcReader = new NfcReader(this.context);
            tagProfile = nfcReader.readFromIntent(getIntent());
        } catch (NoSuchAlgorithmException | IOException | ClassNotFoundException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};

        writeFragment = new NfcWriteDialogFragment();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
        if (isWriting) {
            attemptWriteToTag();
            writeFragment.dismiss();
            isWriting = false;
        } else {
            setIntent(intent);
            tagProfile = nfcReader.readFromIntent(intent);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        writeModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        writeModeOn();
    }

    public void confirmTagWrite() {
        isWriting = true;
        writeFragment.show(getSupportFragmentManager(), "writeTag");
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        isWriting = false;
    }

    protected void attemptWriteToTag() {
        try {
            if (myTag == null) {
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {
                nfcReader.writeToTag(tagProfile, myTag);
                Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
            }
        } catch (IOException | FormatException | IllegalBlockSizeException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    protected void writeModeOn() {
        //    writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    protected void writeModeOff() {
        //    writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

}