package dev.antasource.goling.ui.feature.pickup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dev.antasource.goling.R
import dev.antasource.goling.databinding.ActivityPickupBinding

class PickupActivity : AppCompatActivity() {
    private lateinit var pickupBinding: ActivityPickupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pickupBinding = ActivityPickupBinding.inflate(layoutInflater)
        setContentView(pickupBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(pickupBinding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        pickupBinding.materialToolbar.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        pickupBinding.layoutPickupForm.senderDetail.setOnClickListener{
            val intent = Intent(this, SenderFormActivity::class.java)
            startActivity(intent)
        }

        pickupBinding.layoutPickupForm.recepientDetail.setOnClickListener{
            val intent = Intent(this, RecepientFormActivity::class.java)
            startActivity(intent)
        }

        pickupBinding.layoutPickupForm.itemPackageDetail.setOnClickListener{
            val intent = Intent(this, ItemDetailPackageActivity::class.java)
            startActivity(intent)
        }


    }
}