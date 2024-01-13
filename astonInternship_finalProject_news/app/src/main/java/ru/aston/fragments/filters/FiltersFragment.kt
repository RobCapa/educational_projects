package ru.aston.fragments.filters

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import ru.aston.NewsApp
import ru.aston.R
import ru.aston.data.Filters
import ru.aston.databinding.FragmentFiltersBinding
import ru.aston.interfaces.ui_managing.StatusBarManaging
import ru.aston.interfaces.ui_managing.ToolbarManaging
import ru.aston.utils.DateConverterHelper
import ru.aston.utils.ViewModelFactory
import ru.aston.utils.setBackgroundTint
import java.util.Date
import javax.inject.Inject


class FiltersFragment : Fragment(R.layout.fragment_filters) {

    private val binding: FragmentFiltersBinding by viewBinding()

    private val languageChipsIds = mutableMapOf<Int, Filters.Language>()

    private val viewModel: FiltersViewModel by viewModels { viewModelFactory }

    private var newFilters = Filters()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FiltersViewModel>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as NewsApp).appComponent.inject(this)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            (requireActivity() as? StatusBarManaging)?.showStatusBar(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as? StatusBarManaging)?.showStatusBar(this)

        configureChipGroup()
        configureToolbar()
        observeToLiveData()

        binding.fragFiltersIconButtonChooseDate.setOnClickListener {
            createRangePicker().show(parentFragmentManager, DATE_PICKER_TAG)
        }

        viewModel.getCurrentFilters()
    }

    private fun observeToLiveData() {
        viewModel.currentFilters.observe(viewLifecycleOwner) { filters ->
            newFilters = filters
            bind(filters)
        }
    }

    private fun configureToolbar() {
        with(requireActivity() as ToolbarManaging) {
            val toolbarActions = mapOf(
                ToolbarManaging.ToolbarItem.CHECK_MARK_ITEM to {
                    setNewFilters()
                    requireActivity().onBackPressed()
                },
            )

            setTitleToolbar(
                this@FiltersFragment,
                null,
                getString(R.string.frag_filters),
            )

            addActionsToToolbar(
                this@FiltersFragment,
                toolbarActions,
                false,
            ) { requireActivity().onBackPressed() }
        }
    }

    private fun setNewFilters() {
        with(binding) {
            val sortingStrategy =
                when (binding.fragFiltersButtonToggleGroupSortBy.checkedButtonId) {
                    fragFiltersButtonNew.id -> Filters.SortingStrategy.DATE
                    fragFiltersButtonPopular.id -> Filters.SortingStrategy.POPULARITY
                    fragFiltersButtonRelevant.id -> Filters.SortingStrategy.RELEVANT
                    else -> null
                }

            with(newFilters) {
                language = languageChipsIds[fragFiltersChipGroupLanguages.checkedChipId]
                orderBy = sortingStrategy
                viewModel.setFilters(this)
            }
        }
    }

    private fun bind(filters: Filters) {
        with(filters) {
            with(binding) {
                bindDates(dates)

                orderBy?.let {
                    when (it) {
                        Filters.SortingStrategy.DATE -> fragFiltersButtonNew
                        Filters.SortingStrategy.POPULARITY -> fragFiltersButtonPopular
                        Filters.SortingStrategy.RELEVANT -> fragFiltersButtonRelevant
                    }.isChecked = true
                }

                if (language != null) {
                    languageChipsIds
                        .filter { it.value == language }
                        .keys
                        .first()
                        .let { key -> fragFiltersChipGroupLanguages.check(key) }
                }
            }
        }
    }

    private fun bindDates(dates: kotlin.Pair<Date, Date>?) {
        with(binding) {
            fragFiltersButtonToggleGroupSortBy.addOnButtonCheckedListener { group, checkedId, isChecked ->
                group.findViewById<MaterialButton>(checkedId).icon = if (isChecked) {
                    requireContext().getDrawable(R.drawable.ic_check_mark)
                } else {
                    null
                }
            }

            if (dates != null) {
                val combinedDate = DateConverterHelper.combineDates(
                    kotlin.Pair(dates.first, DateConverterHelper.Formatter.APP_DATE_1),
                    kotlin.Pair(dates.second, DateConverterHelper.Formatter.APP_DATE_2),
                    "-"
                )

                fragFiltersTextViewChooseDate.text = combinedDate
                fragFiltersTextViewChooseDate.setTextColor(requireContext().getColor(R.color.blue_4))
                fragFiltersIconButtonChooseDate.setBackgroundTint(requireContext().getColor(R.color.blue_4))
                fragFiltersIconButtonChooseDate.setIconTintResource(R.color.white)
            } else {
                fragFiltersTextViewChooseDate.text =
                    getString(R.string.fragFilters_textView_chooseDate)
                fragFiltersTextViewChooseDate.setTextColor(requireContext().getColor(R.color.gray_3))
                fragFiltersIconButtonChooseDate.setBackgroundTint(requireContext().getColor(R.color.white))
                fragFiltersIconButtonChooseDate.setIconTintResource(R.color.gray_4)
            }
        }
    }

    private fun createRangePicker(): MaterialDatePicker<Pair<Long, Long>> {
        return MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText(getString(R.string.fragFilters_textView_selectDate))
            .setTheme(R.style.customMaterialCalendarThemeOverlay)
            .setSelection(
                Pair(
                    newFilters.dates?.first?.time,
                    newFilters.dates?.second?.time
                )
            )
            .build()
            .apply {
                addOnPositiveButtonClickListener {
                    val startDate = Date(it.first)
                    val endDate = Date(it.second)
                    val dates = kotlin.Pair(startDate, endDate)
                    newFilters.dates = dates
                    bindDates(dates)
                }

                addOnNegativeButtonClickListener {
                    newFilters.dates = null
                    bindDates(null)
                }
            }
    }

    private fun configureChipGroup() {
        Filters.Language.values().forEach { language ->
            val newId = View.generateViewId()
            languageChipsIds[newId] = language

            val chip = Chip(
                requireContext(),
                null,
                R.attr.customChipFilter
            ).apply {
                text = language.fullName
                checkedIcon = null
                id = newId
            }

            binding.fragFiltersChipGroupLanguages.addView(chip)
        }
    }

    companion object {
        private const val DATE_PICKER_TAG = "DATE_PICKER_TAG"

        fun newInstance(): FiltersFragment {
            return FiltersFragment()
        }
    }
}