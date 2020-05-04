package itesm.juradohja.nfcprototype;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class NfcWriteDialogFragment extends DialogFragment {


    public interface NFCTagWriteDialogListener {
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private NFCTagWriteDialogListener listener;

    private CharSequence msg = new StringBuilder("Acerca la tarjeta NFC al reverso de tu teléfono móvil para guardar los datos en ella.");
    private CharSequence cancel = new StringBuilder("Cancelar");

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NFCTagWriteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("Activity must implement NfcTagWriteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(msg)
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onDialogNegativeClick(NfcWriteDialogFragment.this);
                        dismiss();
                    }
                });
        return builder.create();
    }

}
