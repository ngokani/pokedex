package uk.co.technikhil.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import uk.co.technikhil.pokedex.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var pokemonListAdapter: PokemonListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pokemonListAdapter = PokemonListAdapter()
        pokemonListAdapter.onItemClickedHandler = ::onItemClicked
        binding.pokeList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = pokemonListAdapter
        }

        viewModel.viewState.observe(viewLifecycleOwner, ::onViewStateChanged)
        viewModel.onViewCreated()
    }

    private fun onItemClicked(pokemonName: String) {
        findNavController().navigate(HomeFragmentDirections.actionNavigateToDetails(pokemonName.capitalize()))
    }

    private fun onViewStateChanged(state: PokemonListNetworkState) {
        when (state) {
            is PokemonListNetworkState.Success -> pokemonListAdapter.setPokemonResults(state.pokemonResult)
            is PokemonListNetworkState.Failed -> {}
            is PokemonListNetworkState.Loading -> {}
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}