package com.example.asosspacex.db.dao

import androidx.room.*
import com.example.asosspacex.db.entities.CompanyInfoEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface CompanyInfoDao {
    @Query("SELECT * FROM company_info")
    fun getAll(): Single<List<CompanyInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCompanyInfo(companyInfoEntity: CompanyInfoEntity)

    @Delete
    fun deleteCompanyInfo(companyInfoEntity: CompanyInfoEntity)
}