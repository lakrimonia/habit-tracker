package com.example.habittracker.mainpage

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import com.example.habittracker.ColorPicker
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentHabitsFilterAndSortingBinding
import com.example.habittracker.ApplicationWithDaggerComponent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import javax.inject.Inject

class HabitsFilterAndSortingFragment : Fragment() {
    private var _binding: FragmentHabitsFilterAndSortingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModel: HabitsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val habitsListComponent =
            (requireActivity().application as ApplicationWithDaggerComponent).applicationComponent
                .habitsListComponent()
                .create()
        habitsListComponent.inject(this)
        _binding = FragmentHabitsFilterAndSortingBinding.inflate(inflater, container, false)
        binding.bottomSheetTitle.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        binding.sortByPriorityFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByPriorityAscending()
            binding.sortByDateButtons.clearCheck()
            binding.sortByNameButtons.clearCheck()
        }
        binding.sortByPriorityFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByPriorityDescending()
            binding.sortByDateButtons.clearCheck()
            binding.sortByNameButtons.clearCheck()
        }
        binding.sortByNameFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByNameAscending()
            binding.sortByDateButtons.clearCheck()
            binding.sortByPriorityButtons.clearCheck()
        }
        binding.sortByNameFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByNameDescending()
            binding.sortByDateButtons.clearCheck()
            binding.sortByPriorityButtons.clearCheck()
        }
        binding.sortByDateFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByChangingDateAscending()
            binding.sortByNameButtons.clearCheck()
            binding.sortByPriorityButtons.clearCheck()
        }
        binding.sortByDateFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByChangingDateDescending()
            binding.sortByNameButtons.clearCheck()
            binding.sortByPriorityButtons.clearCheck()
        }
        binding.findByName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let { viewModel.findByName(it) }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.resetAllFilters.setOnClickListener {
            viewModel.clickOnResetFilters()
        }
        ColorPicker.create(requireContext(), resources, binding.colorsScroll) { v ->
            val p = v as ImageButton
            if (p.drawable != null) {
                viewModel.removeColor((p.background as ColorDrawable).color)
                p.setImageDrawable(null)
            } else {
                viewModel.findByColor((p.background as ColorDrawable).color)
                p.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_check_24
                    )
                )
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.resetFilters.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                binding.sortByNameButtons.clearCheck()
                binding.sortByPriorityButtons.clearCheck()
                binding.sortByDateButtons.clearCheck()
                binding.findByName.setText("")
                for (i in 0 until 16) {
                    (binding.colorsScroll.getChildAt(i) as ImageButton).setImageDrawable(null)
                }
            }
        })
    }
}