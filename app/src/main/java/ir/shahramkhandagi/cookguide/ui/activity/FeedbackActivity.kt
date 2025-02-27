package ir.shahramkhandagi.cookguide.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ir.shahramkhandagi.cookguide.R

class FeedbackActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val nameEditText = findViewById<EditText>(R.id.etName)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val descriptionEditText = findViewById<EditText>(R.id.etDescription)
        val sendButton = findViewById<Button>(R.id.btnSendFeedback)
        val backButton = findViewById<ImageView>(R.id.back)



        setSupportActionBar(toolbar)
        supportActionBar?.title = ""


        backButton.setOnClickListener {
            onBackPressed()
        }


        sendButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val description = descriptionEditText.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "لطفا تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendEmail(name, description)
        }
    }


    private fun sendEmail(name: String, description: String) {
        val subject = "Feedback from $name"
        val message = "Name: $name\n\nDescription:\n$description"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("shahramkhandagi@gmail.com")) // ایمیل شما
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        try {
            startActivity(Intent.createChooser(intent, "ارسال ایمیل با:"))
        } catch (e: Exception) {
            Toast.makeText(this, "ایمیل کلاینت یافت نشد", Toast.LENGTH_SHORT).show()
        }
    }
}
