package uk.co.technikhil.pokedex.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import uk.co.technikhil.pokedex.R
import uk.co.technikhil.pokedex.databinding.FragmentPokemonDetailsBinding

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment() {

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val viewModel: PokemonDetailsViewModel by viewModels()

    private val args: PokemonDetailsFragmentArgs by navArgs()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pokemonId = args.pokemonId

        viewModel.viewState.observe(viewLifecycleOwner, ::onViewStateChanged)
        viewModel.getPokemonDetails(pokemonId)
    }

    private fun onViewStateChanged(state: PokemonDetailsNetworkState) {

        if (state is PokemonDetailsNetworkState.Success) {
            binding.apply {
                pokemonName.text = getString(R.string.pokemon_name, state.pokemonResult.name)
                pokemonHeight.text = getString(R.string.pokemon_height, state.pokemonResult.dmHeight.toString())
                pokemonWeight.text = getString(R.string.pokemon_weight, state.pokemonResult.hgWeight.toString())

                Glide
                    .with(this@PokemonDetailsFragment)
                    .load(state.pokemonResult.getImageUrl())
                    .placeholder(android.R.drawable.stat_sys_download)
                    .into(pokemonImage)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}