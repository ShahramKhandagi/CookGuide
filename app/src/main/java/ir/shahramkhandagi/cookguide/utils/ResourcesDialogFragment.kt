package ir.shahramkhandagi.cookguide.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.shahramkhandagi.cookguide.R
import ir.shahramkhandagi.cookguide.adapter.ResourceAdapter

class ResourcesDialogFragment : DialogFragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_resources, null)

        val imageResources = listOf(
            "https://pexels.com",
            "https://wikipedia.org",
            "https://snapp.ir/blog",
            "https://www.youtube.com/@CookingWithYousef"
        )

        val commandResources = listOf(
            "https://chatgpt.com",
            "https://wikipedia.org",
        )

        val fontResources = listOf(
            "https://rastikerdar.github.io/vazirmatn"
        )

        val rvImageResources = view.findViewById<RecyclerView>(R.id.rvImageResources)
        val rvCommandResources = view.findViewById<RecyclerView>(R.id.rvCommandResources)
        val rvFontResources = view.findViewById<RecyclerView>(R.id.rvFontResources)

        rvImageResources.layoutManager = LinearLayoutManager(context)
        rvImageResources.adapter = ResourceAdapter(imageResources, isClickable = true)

        rvCommandResources.layoutManager = LinearLayoutManager(context)
        rvCommandResources.adapter = ResourceAdapter(commandResources, isClickable = true)

        rvFontResources.layoutManager = LinearLayoutManager(context)
        rvFontResources.adapter = ResourceAdapter(fontResources, isClickable = true)

        builder.setView(view)
        builder.setNegativeButton("بستن") { dialog, _ -> dialog.dismiss() }
        return builder.create()
    }

}
