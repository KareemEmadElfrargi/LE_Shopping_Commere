package com.example.ecommercei.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercei.R
import com.example.ecommercei.adapters.BestProductAdapter
import com.example.ecommercei.databinding.FragmentCategoryBaseBinding
import com.example.ecommercei.utils.showButtonNavigationView

open class BaseCategoryFragment(): Fragment() {
    private lateinit var binding : FragmentCategoryBaseBinding
    protected  val offerAdapter : BestProductAdapter by lazy { BestProductAdapter() }
    protected val bestProductAdapter : BestProductAdapter by lazy { BestProductAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBaseBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpOfferRecycleView()
        setUpBestProductRecycleView()


        bestProductAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,bundle)
        }

        offerAdapter.onClick = { product ->
            val bundle = Bundle().apply {
                putParcelable("product",product)
            }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,bundle)
        }


        binding.recycleViewOffer.addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollHorizontally(1) && dx !=0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ view, _, scrollY, _, _ ->

            val canScrollHorizontally = view.canScrollHorizontally(1) || view.canScrollHorizontally(-1)
            val canScrollVertically = view.canScrollVertically(1) || view.canScrollVertically(-1)

            if (canScrollHorizontally || canScrollVertically) {
                // view: This represents the NestedScrollView instance on which the scroll change listener is set
                if (view.getChildAt(0).bottom <= view.height + scrollY) {
                    onBestProductPagingRequest()
                }
            }
        })

    }

    fun showOfferLoading(){

    }
    fun hideOfferLoading(){

    }
    fun showBestProductLoading(){

    }
    fun hideBestProductLoading(){

    }
    open fun onOfferPagingRequest(){

    }
    open fun onBestProductPagingRequest(){

    }

    private fun setUpBestProductRecycleView() {
        binding.recycleViewBestProducts.adapter = bestProductAdapter
    }

    private fun setUpOfferRecycleView() {
        binding.recycleViewOffer.adapter = offerAdapter
    }

    override fun onResume() {
        super.onResume()
        showButtonNavigationView()
    }
}