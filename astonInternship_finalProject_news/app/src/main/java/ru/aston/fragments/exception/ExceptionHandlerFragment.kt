package ru.aston.fragments.exception

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.aston.R
import ru.aston.databinding.FragmentExceptionHandlerBinding
import ru.aston.exceptions.NoConnectionException
import ru.aston.interfaces.ui_managing.RefreshUI
import ru.aston.interfaces.ui_managing.StatusBarManaging

class ExceptionHandlerFragment : Fragment(R.layout.fragment_exception_handler) {

    private val binding: FragmentExceptionHandlerBinding by viewBinding()

    private lateinit var exception: Exception

    override fun onAttach(context: Context) {
        super.onAttach(context)

        exception = arguments?.getParcelable(EXCEPTION_EXTRA, Exception::class.java)
            ?: throw RuntimeException("Missing value by EXCEPTION_EXTRA")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden) {
            (requireActivity() as? StatusBarManaging)?.showStatusBar(this)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
    }

    private fun bind() {
        with(binding) {
            fragExceptionHandlerTextViewMessage.text = getMessage(exception)
            fragExceptionHandlerButtonRefresh.setOnClickListener {
                (parentFragment as RefreshUI).refresh()
            }
        }
    }

    private fun getMessage(exception: Exception): String {
        return when (exception.cause ?: exception) {
            is NoConnectionException -> R.string.errorMessage_noConnection
            else -> R.string.errorMessage_unknownError
        }.let { getString(it) }
    }

    companion object {
        private const val EXCEPTION_EXTRA = "EXCEPTION_EXTRA"

        @JvmStatic
        fun newInstance(
            exception: Exception,
        ): ExceptionHandlerFragment {
            return ExceptionHandlerFragment().apply {
                arguments = bundleOf(
                    EXCEPTION_EXTRA to exception,
                )
            }
        }
    }
}