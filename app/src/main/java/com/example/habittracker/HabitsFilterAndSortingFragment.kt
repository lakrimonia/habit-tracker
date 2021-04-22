package com.example.habittracker

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import com.example.habittracker.databinding.FragmentHabitsFilterAndSortingBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HabitsFilterAndSortingFragment : Fragment() {
    private var _binding: FragmentHabitsFilterAndSortingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HabitsListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHabitsFilterAndSortingBinding.inflate(inflater, container, false)
        binding.bottomSheetTitle.setOnClickListener {
            val bottomSheetBehavior = BottomSheetBehavior.from(binding.root.parent as View)
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            else bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        binding.sortByPriorityFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByPriorityAscending()
        }
        binding.sortByPriorityFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByPriorityDescending()
        }
        binding.sortByNameFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByNameAscending()
        }
        binding.sortByNameFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByNameDescending()
        }
        binding.sortByDateFromLowToHigh.setOnClickListener {
            viewModel.sortHabitsByCreatingDateAscending()
        }
        binding.sortByDateFromHighToLow.setOnClickListener {
            viewModel.sortHabitsByCreatingDateDescending()
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
        createColorTable()
//        val check = ContextCompat.getDrawable(
//            binding.root.context,
//            R.drawable.ic_baseline_check_24
//        )
//        ColorPicker.create(requireContext(),resources, binding.colorsScroll) {
//            val button = it as ImageButton
//            if (button.drawable != null) {
//                viewModel.removeColor((button.background as ColorDrawable).color)
//                button.setImageDrawable(null)
//            } else {
//                viewModel.findByColor((button.background as ColorDrawable).color)
//                button.setImageDrawable(ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.ic_baseline_check_24
//                ))
//            }
//        }
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

    private fun createColorTable() {
        val colors = intArrayOf(
            Color.parseColor("#ff0000"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#00ff00"),
            Color.parseColor("#00ffff"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#ff00ff"),
            Color.parseColor("#ff0000")
        )
        val gradient = GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors)
        val colorButtonWidth = 50
        val width = 2 * colorButtonWidth * 16
        val height = 100
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        gradient.setBounds(0, 0, width, height)
        gradient.draw(c)
        binding.colorsScroll.background = b.toDrawable(resources)
        for (i in 0 until 16) {
            val button = ImageButton(context)
            val buttonParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            val displayMetrics = resources.displayMetrics
            buttonParams.height =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    colorButtonWidth.toFloat(),
                    displayMetrics
                )
                    .toInt()
            buttonParams.width =
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    colorButtonWidth.toFloat(),
                    displayMetrics
                )
                    .toInt()
            buttonParams.rightMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                colorButtonWidth.toFloat(),
                displayMetrics
            )
                .toInt()
            buttonParams.topMargin = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                20f,
                displayMetrics
            )
                .toInt()
            button.layoutParams = buttonParams
            val x = 2 * colorButtonWidth * i + colorButtonWidth / 2
            val y = 0
            val color = ColorDrawable(b.getPixel(x, y))
            button.background = color
            ViewCompat.setElevation(button, 5f)
            button.setOnClickListener { v ->
                val p = v as ImageButton
                if(p.drawable!=null) {
                    viewModel.removeColor((p.background as ColorDrawable).color)
                    p.setImageDrawable(null)
                }
                else {
                    viewModel.findByColor((p.background as ColorDrawable).color)
                    p.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_baseline_check_24
                        )
                    )
                }
            }
            binding.colorsScroll.addView(button)
        }
    }
}