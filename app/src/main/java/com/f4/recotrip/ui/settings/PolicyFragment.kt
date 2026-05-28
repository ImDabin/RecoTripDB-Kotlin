package com.f4.recotrip.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.f4.recotrip.R

class PolicyFragment : Fragment() {

    companion object {
        fun newInstance(title: String, content: String): PolicyFragment {
            val fragment = PolicyFragment()
            val args = Bundle()
            args.putString("title", title)
            args.putString("content", content)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.settings_fragment_policy, container, false)

        val title = arguments?.getString("title") ?: "정책"
        val content = arguments?.getString("content") ?: ""

        view.findViewById<TextView>(R.id.policy_title).text = title
        view.findViewById<TextView>(R.id.policy_text).text = content

        view.findViewById<ImageButton>(R.id.policy_close).setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@PolicyFragment).commit()
        }

        return view
    }
}