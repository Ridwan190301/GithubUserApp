package com.dicoding.consumerfavoriteapp.detailuser.activity

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.consumerfavoriteapp.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.consumerfavoriteapp.MappingHelper
import com.dicoding.consumerfavoriteapp.R
import com.dicoding.consumerfavoriteapp.User
import com.dicoding.consumerfavoriteapp.databinding.ActivityDetailUserBinding
import com.dicoding.consumerfavoriteapp.detailuser.adapter.SectionPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private var dataUserFav: User? = null
    private var position: Int = 0
    private var data = arrayListOf<String>()
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataUserFav = intent.getParcelableExtra(EXTRA_FAVORITE)
        detailUser(dataUserFav)
        position = intent.getIntExtra(EXTRA_POSITION, 0)

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUserFav?.id)

        val cursor = contentResolver.query(uriWithId, null, null,
            null, null)

        if (cursor != null) {
            dataUserFav = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        binding.btnFollow.setOnClickListener(this)
        binding.btnUnfollow.setOnClickListener(this)
        binding.btnFavorite.setOnClickListener(this)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.data = data
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_follow -> {
                val addFollowers = data[0].toInt() + 1
                binding.tvFollowers.text = addFollowers.toString()
                binding.btnFollow.visibility = View.GONE
                binding.btnUnfollow.visibility = View.VISIBLE
            }
            R.id.btn_unfollow -> {
                binding.tvFollowers.text = data[0]
                binding.btnFollow.visibility = View.VISIBLE
                binding.btnUnfollow.visibility = View.GONE
            }
            R.id.btn_favorite -> {
                Toast.makeText(this, "Data sudah ditambahkan difavorit",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun detailUser(dataUser: User?) {
        data.add(dataUser?.followers.toString())
        data.add(dataUser?.following.toString())
        data.add(dataUser?.username.toString())

        supportActionBar?.title = dataUser?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailUserViewModel::class.java)

        detailUserViewModel.setDetailUser(dataUser?.username.toString())

        detailUserViewModel.getDetailUsers().observe(this, {
                userItems -> if (userItems != null) {
            Glide.with(this)
                .load(userItems.avatar)
                .into(binding.imgAvatarReceived)

            binding.tvNameReceived.text = userItems.name
            binding.tvCompanyReceived.text = userItems.company
            binding.tvLocationReceived.text = userItems.location
            binding.tvRepository.text = userItems.repository.toString()
            binding.tvFollowing.text = userItems.following.toString()
            binding.tvFollowers.text = userItems.followers.toString()
        }
        })
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE

        if (isDialogClose) {
            finish()
        } else {
            val dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            val dialogTitle = "Hapus Note"

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle(dialogTitle)
            alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    contentResolver.delete(uriWithId, null, null)
                    Toast.makeText(this@DetailUserActivity, "Data berhasil dihapus",
                        Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }
}