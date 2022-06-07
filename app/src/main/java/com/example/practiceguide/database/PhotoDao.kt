package com.example.practiceguide.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.practiceguide.models.Photo

@Dao
interface PhotoDao {
    @Query("SELECT * FROM Photo")
    fun getAllPhotoFromDB(): List<Photo>

    @Insert
    fun insertPhoto(vararg photo: Photo)
}