package com.miempresa.jsonplaceapp.data.local.dao

import androidx.room.*
import com.miempresa.jsonplaceapp.data.local.entity.PhotoEntity

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity)

    @Query("SELECT * FROM photos WHERE albumId = :albumId ORDER BY id ASC")
    suspend fun getPhotosByAlbumId(albumId: Int): List<PhotoEntity>

    @Query("SELECT * FROM photos WHERE id = :photoId")
    suspend fun getPhotoById(photoId: Int): PhotoEntity?

    @Query("DELETE FROM photos")
    suspend fun clearAll()
}