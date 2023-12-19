package id.ac.unri.driverfit.ui.onboarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import id.ac.unri.driverfit.databinding.ItemOnboardingBinding
import id.ac.unri.driverfit.domain.model.Onboarding

class OnboardingAdapter(
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    private val data = mutableListOf<Onboarding>()

    class OnboardingViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(starter: Onboarding) = with(binding) {
            imageStarter.setImageResource(starter.image)
            judulStarter.text = starter.judul
            deskStarter.text = starter.desk
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemOnboardingBinding.inflate(inflater, parent, false)
        return OnboardingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateData(newData: List<Onboarding>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}