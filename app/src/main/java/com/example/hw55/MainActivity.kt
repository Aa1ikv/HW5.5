@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecycler()

        // Получаем персонажей с API
        viewModel.getCharacters()

        // Загружаем сохранённых персонажей из базы данных
        viewModel.loadViewedCharacters()

        viewModel.charactersData.observe(this) { data ->
            // Обработка данных с API
            data?.let {
                // Вы можете сохранить персонажей
                it.forEach { character ->
                    viewModel.saveCharacter(character)
                }
            }
        }

        viewModel.viewedCharacters.observe(this) { viewedCharacters ->
            // Обновляем RecyclerView с сохранёнными персонажами
            (binding.rvCharacters.adapter as? CharacterAdapter)?.submitList(viewedCharacters)
        }
    }

    private fun setupRecycler() = with(binding) {
        rvCharacters.adapter = CharacterAdapter()
        rvCharacters.layoutManager = LinearLayoutManager(
            this@MainActivity,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}
