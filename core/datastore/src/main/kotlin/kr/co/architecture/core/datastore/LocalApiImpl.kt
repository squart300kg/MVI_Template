package kr.co.architecture.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kr.co.architecture.core.datastore.model.StringMediaContentsEntity
import javax.inject.Inject

class LocalApiImpl @Inject constructor(
  private val dataStore: DataStore<Preferences>
) : LocalApi {

  private val gson by lazy(LazyThreadSafetyMode.NONE) { Gson() }

  override fun observeBookmarkedMedias(): Flow<Set<MediaContentsEntity>> =
    dataStore.data.map { preference ->
      val stringEntities = preference[MEDIA_CONTENTS_LIST] ?: emptySet()

      buildSet(stringEntities.size) {
        for (stringEntity in stringEntities) {
          val parsed = try {
            MediaContentsEntity.mapperToEntity(StringMediaContentsEntity(stringEntity))
          } catch (_: Exception) {
            null
          }
          if (parsed != null) add(parsed)
        }
      }
    }

  override suspend fun delete(entity: MediaContentsEntity) {
    val targetId = entity.uniqueId()

    dataStore.edit { preferences ->
      val stringEntities = preferences[MEDIA_CONTENTS_LIST] ?: emptySet()

      // 기존 요소 한 번만 순회
      val updated = buildSet(stringEntities.size) {
        for (stringEntity in stringEntities) {
          val isSame = try {
            val entity = MediaContentsEntity.mapperToEntity(StringMediaContentsEntity(stringEntity))
            entity.uniqueId() == targetId
          } catch (_: Exception) { false }

          if (!isSame) add(stringEntity)
        }
      }

      preferences[MEDIA_CONTENTS_LIST] = updated
    }
  }

  override suspend fun upsert(entity: MediaContentsEntity) {
    val targetId = entity.uniqueId()
    val targetJson = gson.toJson(entity)

    dataStore.edit { preferences ->
      val stringEntities = preferences[MEDIA_CONTENTS_LIST] ?: emptySet()

      val updated = buildSet(stringEntities.size + 1) {
        // 기존 요소 한 번만 순회: 동일 uniqueId만 제외하고 모두 유지
        for (stringEntity in stringEntities) {
          val isSame = try {
            val entity = MediaContentsEntity.mapperToEntity(StringMediaContentsEntity(stringEntity))
            entity.uniqueId() == targetId
          } catch (_: Exception) { false }

          if (!isSame) add(stringEntity)
        }
        // 새 값은 마지막에 딱 한 번 추가 (없으면 추가, 있으면 교체 효과)
        add(targetJson)
      }

      preferences[MEDIA_CONTENTS_LIST] = updated
    }
  }

  companion object {
    const val SHARED_PREFS_NAME = "SSY_APP"

    val MEDIA_CONTENTS_LIST = stringSetPreferencesKey("mediaContentsList")

  }
}