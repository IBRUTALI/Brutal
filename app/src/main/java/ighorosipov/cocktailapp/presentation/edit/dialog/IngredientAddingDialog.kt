package ighorosipov.cocktailapp.presentation.edit.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import ighorosipov.cocktailapp.R
import ighorosipov.cocktailapp.databinding.DialogLayoutBinding

class IngredientAddingDialog(
    private val onAddIngredient: (String) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogLayoutBinding.inflate(layoutInflater)
        with (binding) {
            titleInputLayout.editText?.doAfterTextChanged {
                titleInputLayout.error = null
            }
            addButton.setOnClickListener {
                if (titleInputLayout.editText == null || titleInputLayout.editText?.text?.isBlank() == true) {
                    titleInputLayout.error = getString(R.string.add_title)
                } else {
                    onAddIngredient.invoke(titleInputLayout.editText!!.text.toString())
                    dismiss()
                }
            }
            cancelButton.setOnClickListener {
                dismiss()
            }
        }

        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)

        val alertDialog = builder.create()
        val drawable: Drawable? = AppCompatResources.getDrawable(requireContext(), R.drawable.shape_radius_54)
        alertDialog.window?.setBackgroundDrawable(drawable)

        return alertDialog
    }

}