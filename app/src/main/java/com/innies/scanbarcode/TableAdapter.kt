package com.innies.scanbarcode

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innies.scanbarcode.databinding.ActivityVerifyBarcodeBinding
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class TableAdapter(private val tableData: List<TableRowData>) :
    RecyclerView.Adapter<TableAdapter.TableViewHolder>() {

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvColumn1: TextView = itemView.findViewById(R.id.tvColumn1)
        val tvColumn2: TextView = itemView.findViewById(R.id.tvColumn2)
        val tvColumn3: TextView = itemView.findViewById(R.id.tvColumn3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_table_item, parent, false)
        return TableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        val rowData = tableData[position]
        holder.tvColumn3.visibility = View.VISIBLE
        holder.tvColumn1.text = rowData.barcode1
        holder.tvColumn2.text = rowData.barcode2
        holder.tvColumn3.text = rowData.timeStamp
    }

    override fun getItemCount(): Int {
        return tableData.size
    }
}
