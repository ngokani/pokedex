package uk.co.technikhil.pokedex.ui.home

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import uk.co.technikhil.pokedex.data.PokemonResult
import uk.co.technikhil.pokedex.databinding.ViewPokemonItemBinding

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.PokemonListItemViewHolder>() {

    private var pokemonList = mutableListOf<PokemonResult>()

    var onItemClickedHandler: ((Int) -> Unit)? = null

    fun setPokemonResults(results: List<PokemonResult>) {

        pokemonList = results.toMutableList()
        notifyDataSetChanged()
    }

    fun addPokemonResult(result: PokemonResult) {

        pokemonList.add(result)
        notifyItemInserted(pokemonList.size - 1)
    }

    fun removePokemonResult(result: PokemonResult) {

        val index = pokemonList.indexOf(result)
        if (index >= 0) {
            pokemonList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun removePokemonResult(index: Int) {

        if (index >= 0 && index < pokemonList.size) {
            pokemonList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int = pokemonList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListItemViewHolder {
        val viewPokemonItemBinding = ViewPokemonItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return PokemonListItemViewHolder(viewPokemonItemBinding)
    }

    override fun onBindViewHolder(holder: PokemonListItemViewHolder, position: Int) {
        holder.pokemonName.text = pokemonList[position].name

        Glide
            .with(holder.itemView.context)
            .load(pokemonList[position].imageUrl)
            .placeholder(android.R.drawable.stat_sys_download)
            .into(holder.pokemonImage)

        holder.itemView.setOnClickListener {
            onItemClickedHandler?.invoke(pokemonList[position].id)
        }
    }

    class PokemonListItemViewHolder(viewPokemonItemBinding: ViewPokemonItemBinding) :
        ViewHolder(viewPokemonItemBinding.root) {

        val pokemonName: TextView = viewPokemonItemBinding.pokemonName
        val pokemonImage: ImageView = viewPokemonItemBinding.pokemonImage
    }
}
