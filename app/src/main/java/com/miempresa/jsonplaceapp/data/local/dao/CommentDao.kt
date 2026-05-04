package com.miempresa.jsonplaceapp.data.local.dao

import androidx.room.*
import com.miempresa.jsonplaceapp.data.local.entity.CommentEntity

@Dao
interface CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(comments: List<CommentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: CommentEntity)

    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY id ASC")
    suspend fun getCommentsByPostId(postId: Int): List<CommentEntity>

    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun deleteCommentsByPostId(postId: Int)

    @Query("DELETE FROM comments")
    suspend fun clearAll()
}