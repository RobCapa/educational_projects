package com.example.animationsamles

import android.animation.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.animationsamles.databinding.FragmentAnimationBinding

class AnimationFragment : Fragment(R.layout.fragment_animation) {
    private lateinit var binding: FragmentAnimationBinding

    private val textView
        get() = binding.textView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnimationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.animateButton.setOnClickListener { animateText() }
        binding.animateButton2.setOnClickListener { animateText2() }
        binding.animateButton3.setOnClickListener { animateText3() }
        binding.animateButton4.setOnClickListener { animateText4() }
        binding.animateButton5.setOnClickListener { animateText5() }
    }

    private fun animateText() {
        textView
            .animate()
            .scaleX(2.0F)
            .scaleY(2.0F)
            .setDuration(1000L)
            .withEndAction {
                textView
                    .animate()
                    .scaleX(1.0F)
                    .scaleY(1.0F)
                    .setDuration(1000L)
                    .start()
            }.start()
    }

    private fun animateText2() {
        val scaleX = PropertyValuesHolder.ofFloat("scaleX", 2.0F)
        val scaleY = PropertyValuesHolder.ofFloat("scaleY", 2.0F)

        val obj = ObjectAnimator.ofPropertyValuesHolder(textView, scaleX, scaleY)
obj.addListener(
    object : Animator.AnimatorListener {
        override fun onAnimationStart(p0: Animator?) = Unit

        override fun onAnimationEnd(p0: Animator?) {
            val scaleX2 = PropertyValuesHolder.ofFloat("scaleX", 1.0F)
            val scaleY2 = PropertyValuesHolder.ofFloat("scaleY", 1.0F)
            ObjectAnimator
                .ofPropertyValuesHolder(textView, scaleX2, scaleY2)
                .setDuration(1000L)
                .start()
        }

        override fun onAnimationCancel(p0: Animator?) = Unit
        override fun onAnimationRepeat(p0: Animator?) = Unit
    }
)
        obj.setDuration(1000L).start()
    }

    private fun animateText3() {
        val scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 2.0F).setDuration(1000L)
        val scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 2.0F).setDuration(1000L)

        val scaleX2 = ObjectAnimator.ofFloat(textView, "scaleX", 1.0F).setDuration(1000L)
        val scaleY2 = ObjectAnimator.ofFloat(textView, "scaleY", 1.0F).setDuration(1000L)

        AnimatorSet().apply {
            play(scaleX).with(scaleY)
            play(scaleX2).with(scaleY2)
            play(scaleX2).after(scaleX)
            interpolator = BounceInterpolator()
            start()
        }
    }

    private fun animateText4() {
        val text = "Animate this text"

        val animator = ValueAnimator.ofFloat(0F, 1F)
        animator.addUpdateListener { valueAnimator ->
            textView.text = text.subSequence(
                startIndex = 0,
                endIndex = (valueAnimator.animatedFraction * text.length).toInt(),
            )
        }
        animator.setDuration(1000L).start()
    }

    private fun animateText5() {
        val animator = AnimatorInflater.loadAnimator(
            requireContext(),
            R.animator.animation_scale
        ) as AnimatorSet
        animator.setTarget(textView)
        animator.start()
    }
}