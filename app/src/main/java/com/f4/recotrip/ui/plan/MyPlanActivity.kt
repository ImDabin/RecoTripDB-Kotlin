package com.f4.recotrip.ui.plan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.f4.recotrip.databinding.PlanActivityMyplansBinding
import com.f4.recotrip.R

class MyPlanActivity : AppCompatActivity() {

    private lateinit var binding: PlanActivityMyplansBinding
    private lateinit var adapter: SavePlanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PlanActivityMyplansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dummyList = SaveMyPlan.getDummyPlans()

        adapter = SavePlanAdapter(dummyList) { selectedPlan ->
            val fragment = PlanDetailFragment()
            fragment.selectedPlan = selectedPlan // ✅ 직접 전달

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewMyPlans.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMyPlans.adapter = adapter
    }
}