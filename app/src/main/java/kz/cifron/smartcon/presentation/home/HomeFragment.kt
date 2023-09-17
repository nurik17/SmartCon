package kz.cifron.smartcon.presentation.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kz.cifron.smartcon.R
import kz.cifron.smartcon.databinding.FragmentHomeBinding
import java.util.Locale


class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var taskViewModel : TaskViewModel
    private lateinit var taskAdapter: TaskAdapter

    private var originalTaskList: List<Tasks> = emptyList()

    private var requestCamera : ActivityResultLauncher<String>? = null

    private lateinit var drawerLayout: DrawerLayout




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        val taskApiService = TaskRetrofitClient.taskInstanceApi
        val taskRepository = TaskRepository(taskApiService)
        taskViewModel = ViewModelProvider(this, TaskViewModelFactory(taskRepository))[TaskViewModel::class.java]


        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(),){
            if(it){
                findNavController().navigate(R.id.action_id_homeFragment_to_scannerFragment)
            }else{
                Toast.makeText(requireContext(), "camera(scan) permission not granted", Toast.LENGTH_SHORT).show()
            }
            binding.fabBtn.setOnClickListener {
                requestCamera?.launch(android.Manifest.permission.CAMERA)
            }
            binding.fabBtn.setImageResource(R.drawable.ic_scan)
        }

        drawerLayout = binding.drawerlayout


        return binding.root
    }

    @SuppressLint("RtlHardcoded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeTask()
        taskViewModel.getTasks()
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.fabBtn.setOnClickListener {
            findNavController().navigate(R.id.action_id_homeFragment_to_scannerFragment)
        }

        taskAdapter.setOnItemClickListener {task ->
            val bundle = Bundle()
            bundle.putParcelable("task",task)
            Log.d("HomeFragment", "Task addr : ${task.ADDR}")
            findNavController().navigate(R.id.action_id_homeFragment_to_counterFragment, bundle)
        }

/*
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val drawerLayout = binding.drawerlayout
            if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                // Если боковое меню открыто, закройте его
                drawerLayout.closeDrawer(Gravity.LEFT)
            } else {
                // В противном случае выполните навигацию "назад"
                findNavController().popBackStack()
            }
        }
*/


        binding.menuButton.setOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }

        val navigationView = binding.navigationView

        navigationView.setNavigationItemSelectedListener { menuItem->
            when(menuItem.itemId){
                R.id.menu_anonim ->{
                    findNavController().navigate(R.id.tabLayoutFragment)
                    drawerLayout.closeDrawer(Gravity.LEFT)
                    true
                }
                R.id.menu_acts ->{
                    findNavController().navigate(R.id.actFragment)
                    drawerLayout.closeDrawer(Gravity.LEFT)
                    true
                }
                R.id.menu_ended ->{
                    findNavController().navigate(R.id.endedActsFragment)
                    drawerLayout.closeDrawer(Gravity.LEFT)
                    true
                }
                else -> false
            }
        }

    }

    private fun setUpRecyclerView(){
        taskAdapter = TaskAdapter()
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = taskAdapter
    }

    private fun observeTask(){
        taskViewModel.taskLiveData.observe(viewLifecycleOwner){state->
            when(state){
                is TaskState.Loading ->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is TaskState.Success ->{
                    binding.progressBar.visibility = View.INVISIBLE
                    originalTaskList = state.tasks
                    taskAdapter.differ.submitList(originalTaskList)
                }
                is TaskState.Error->{
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Api is error data holded", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun performSearch(query: String) {
        val filteredList = originalTaskList.filter { task ->
            task.ADDR.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))
        }
        taskAdapter.differ.submitList(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by setting _binding to null in onDestroyView
    }
}