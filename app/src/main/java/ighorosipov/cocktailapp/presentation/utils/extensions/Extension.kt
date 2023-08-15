package ighorosipov.cocktailapp.presentation.utils.extensions

import android.app.AlertDialog
import android.widget.Toast
import androidx.fragment.app.Fragment
import ighorosipov.cocktailapp.R

fun Fragment.showToast(
    message: String,
    longLength: Boolean = false
) {
    Toast.makeText(
        requireContext(),
        message,
        if (longLength) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

fun Fragment.showAlert(
    title: String,
    message: String,
    onClick: () -> Unit = { }
) {
    AlertDialog.Builder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ ->
            onClick.invoke()
        }
        .setNegativeButton(getString(R.string.no)) { _, _ -> }
        .show()
}
