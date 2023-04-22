package uk.co.technikhil.pokedex.ui.moves

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import uk.co.technikhil.pokedex.data.Move
import uk.co.technikhil.pokedex.data.PokemonMove

class MovesAdapter : RecyclerView.Adapter<MovesAdapter.MoveItemViewHolder>() {

    private var pokemonMovesList = listOf<PokemonMove>()

    fun setPokemonMovesList(pokemonMovesList: List<PokemonMove>) {
        this.pokemonMovesList = pokemonMovesList
        notifyDataSetChanged()
    }

    var onItemClickedHandler: ((Move) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveItemViewHolder {
        return MoveItemViewHolder(TextView(parent.context))
    }

    override fun getItemCount(): Int = pokemonMovesList.size

    override fun onBindViewHolder(holder: MoveItemViewHolder, position: Int) {
        val pokemonMove = pokemonMovesList[position]
        holder.textView.text = pokemonMove.move.name.replace("-", " ").capitalize()
        holder.textView.setOnClickListener {
            onItemClickedHandler?.invoke(pokemonMove.move)
        }
    }

    class MoveItemViewHolder(val textView: TextView) : ViewHolder(textView)
}