package supdevinci.vitemeteo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import supdevinci.vitemeteo.R
import supdevinci.vitemeteo.viewmodel.ViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var viewModel = ViewModel();

        displayToast(viewModel.getPositionToString())
    }

    private fun displayToast(text:String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}