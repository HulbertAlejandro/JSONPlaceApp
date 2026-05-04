package com.miempresa.jsonplaceapp.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.miempresa.jsonplaceapp.data.local.entity.PostEntity

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    // PagingSource nativo — Room genera la implementación automáticamente
    @Query("SELECT * FROM posts ORDER BY id ASC")
    fun getPostsPaged(): PagingSource<Int, PostEntity>

    // Filtro por userId para el buscador (campo 1)
    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY id ASC")
    fun getPostsByUserIdPaged(userId: Int): PagingSource<Int, PostEntity>

    // Filtro por título (campo 2 del buscador, LIKE para búsqueda parcial)
    @Query("SELECT * FROM posts WHERE title LIKE '%' || :query || '%' ORDER BY id ASC")
    fun getPostsByTitlePaged(query: String): PagingSource<Int, PostEntity>

    // Filtro combinado: userId + título
    @Query("""
        SELECT * FROM posts 
        WHERE userId = :userId AND title LIKE '%' || :query || '%' 
        ORDER BY id ASC
    """)
    fun getPostsByUserIdAndTitlePaged(userId: Int, query: String): PagingSource<Int, PostEntity>

    @Query("SELECT * FROM posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): PostEntity?

    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getPostCount(): Int

    @Query("DELETE FROM posts")
    suspend fun clearAll()
}