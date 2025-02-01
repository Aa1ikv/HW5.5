import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@Entity(tableName = "viewed_characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val imageBase64: String
)

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("SELECT * FROM viewed_characters")
    fun getViewedCharacters(): Flow<List<CharacterEntity>>
}

@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun base64ToBitmap(base64String: String): Bitmap {
    val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

class MainViewModel @Inject constructor(
    private val api: CartoonApiService,
    private val database: AppDatabase
) : ViewModel() {

    private val _charactersData = MutableLiveData<List<Character>?>()
    val charactersData: MutableLiveData<List<Character>?>
        get() = _charactersData

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> get() = _errorData

    fun getCharacters() {
        api.getCharacters().enqueue(object : retrofit2.Callback<BaseResponce> {
            override fun onResponse(call: Call<BaseResponce>, response: Response<BaseResponce>) {
                if (response.isSuccessful && response.body() != null) {
                    response.body()?.let {
                        _charactersData.postValue(it.characters)
                    }
                }
            }

            override fun onFailure(call: Call<BaseResponce>, t: Throwable) {
                _errorData.postValue(t.localizedMessage ?: "Unknown error")
            }
        })
    }

    fun saveCharacter(character: Character) {
        viewModelScope.launch {
            val imageBase64 = bitmapToBase64(character.imageBitmap)
            val entity = CharacterEntity(
                id = character.id,
                name = character.name,
                status = character.status,
                imageBase64 = imageBase64
            )
            database.characterDao().insertCharacter(entity)
        }
    }
}
