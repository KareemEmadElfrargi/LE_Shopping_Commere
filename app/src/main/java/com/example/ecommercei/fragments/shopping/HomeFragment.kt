package com.example.ecommercei.fragments.shopping

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.ecommercei.R
import com.example.ecommercei.adapters.HomeViewPagerAdapter
import com.example.ecommercei.databinding.FragmentHomeBinding
import com.example.ecommercei.fragments.categories.AccessoryFragment
import com.example.ecommercei.fragments.categories.ChairFragment
import com.example.ecommercei.fragments.categories.CupboardFragment
import com.example.ecommercei.fragments.categories.FurnitureFragment
import com.example.ecommercei.fragments.categories.MainCategoryFragment
import com.example.ecommercei.fragments.categories.TableFragment
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf<Fragment>(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment(),
        )
        binding.viewPager2Home.isUserInputEnabled = false
        //val customTypeface = ResourcesCompat.getFont(requireContext(), R.font.poppins)

        val viewPager2Adapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)

        binding.viewPager2Home.adapter = viewPager2Adapter
        TabLayoutMediator(binding.tabLayout,binding.viewPager2Home){ tab , position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()

    }
}