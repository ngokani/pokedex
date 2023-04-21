package uk.co.technikhil.pokedex.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}