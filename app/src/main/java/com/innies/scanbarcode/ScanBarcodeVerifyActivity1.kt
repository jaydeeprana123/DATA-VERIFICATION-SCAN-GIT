//package com.innies.scanbarcode
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.Dialog
//import android.content.ActivityNotFoundException
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.media.MediaPlayer
//import android.media.MediaScannerConnection
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.os.Handler
//import android.provider.Settings
//import android.util.Log
//import android.view.View
//import android.view.ViewGroup
//import android.view.inputmethod.EditorInfo
//import android.view.inputmethod.InputMethodManager
//import android.widget.Button
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.FileProvider
//import androidx.core.widget.doOnTextChanged
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.innies.scanbarcode.databinding.ActivityVerifyBarcodeBinding
//import java.io.BufferedReader
//import java.io.BufferedWriter
//import java.io.File
//import java.io.FileOutputStream
//import java.io.FileReader
//import java.io.FileWriter
//import java.text.SimpleDateFormat
//import java.time.LocalDate
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//import java.util.Date
//import java.util.Locale
//
//
//class ScanBarcodeVerifyActivity1 : AppCompatActivity() {
//
//
//    private var totalRows = 0
//    private var firstColumnMatched = false
//    private var secondColumnMatched = false
//    private var thirdColumnMatched = false
//    private lateinit var binding: ActivityVerifyBarcodeBinding
//    private val CSV_FILE_NAME = "data.csv" // Change as needed
//    val matchedData = mutableListOf<String>()
//
//    /// This is for check duplicate entry
//    val barcodeData = mutableListOf<TableRowData>()
//    val remainingData = mutableListOf<TableRowData>()
//    var matchedRowPosition: Int? = null
//
//    var savedFile: File? = null
//
//    lateinit var mediaPlayerSuccess: MediaPlayer
//    lateinit var mediaPlayerFail: MediaPlayer
//
//    private val PICK_CSV_REQUEST = 1  // Request code for CSV file selection
//
//    @RequiresApi(Build.VERSION_CODES.R)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityVerifyBarcodeBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        Log.e("here", "Come")
//
//        createFolders()
//        mediaPlayerSuccess = MediaPlayer.create(this, R.raw.success);
//        mediaPlayerFail = MediaPlayer.create(this, R.raw.fail);
//
//        requestStoragePermission()
//        requestAllFilesAccess()
//
//        if (savedFile != null) {
//            binding.tvResult.visibility = View.VISIBLE
//            binding.btnScanFile.text = savedFile?.name
//            binding.tvResult.text = "Now Scan Barcode"
//        }
//
//
//        binding.btnScanFile.setOnClickListener {
//            openFile()
//
////            openFilePicker()
//        }
//
//
//        binding.btnOpenCsv.setOnClickListener {
//
//            val folder = File(
//                Environment.getExternalStorageDirectory(),
//                "output"
//            )
//
//
//            // Define file path
//            val file = File(folder, savedFile?.name)
//
//
//
//            openCsvFile(this, file)
//
//
//            // Download the saved CSV file
//            //  downloadMatchedCsv(this)
//        }
//
//
//        binding.btnScannedData.setOnClickListener {
//
//            showTableDialog(this)
//        }
//
//
//        binding.btnRemainingData.setOnClickListener {
//            showRemainingTableDialog(this)
//        }
//
//        binding.edtScanQrHere.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE ||
//                actionId == EditorInfo.IME_ACTION_NEXT ||
//                actionId == EditorInfo.IME_ACTION_SEARCH
//            ) {
//
//
//                if (savedFile == null) {
//                    Toast.makeText(this, "Please select CSV file", Toast.LENGTH_SHORT).show()
//                } else {
//                    readCsvFile(savedFile!!, 1, binding.edtScanQrHere.text.toString())
//                }
//                true // Return true to indicate the event is handled
//            } else {
//                false
//            }
//        }
//
//        binding.edtScanQrHere.doOnTextChanged { text, start, before, count ->
//            binding.edtData1.setText("")
//            binding.edtData2.setText("")
//            binding.clMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.mainScroll.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.tvResult.setTextColor(Color.parseColor("#000000"))
//            binding.tvScannedNumber.setTextColor(Color.parseColor("#000000"))
//            binding.tvBarcode.setTextColor(Color.parseColor("#000000"))
//            binding.tvData1.setTextColor(Color.parseColor("#000000"))
//            binding.tvData2.setTextColor(Color.parseColor("#000000"))
//        }
//
//        binding.edtData1.doOnTextChanged { text, start, before, count ->
//            binding.clMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.mainScroll.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.tvResult.setTextColor(Color.parseColor("#000000"))
//            binding.tvScannedNumber.setTextColor(Color.parseColor("#000000"))
//            binding.tvBarcode.setTextColor(Color.parseColor("#000000"))
//            binding.tvData1.setTextColor(Color.parseColor("#000000"))
//            binding.tvData2.setTextColor(Color.parseColor("#000000"))
//
//        }
//
//        binding.edtData2.doOnTextChanged { text, start, before, count ->
//            binding.clMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.mainScroll.setBackgroundColor(Color.parseColor("#FFFFFF"))
//            binding.tvResult.setTextColor(Color.parseColor("#000000"))
//            binding.tvScannedNumber.setTextColor(Color.parseColor("#000000"))
//            binding.tvBarcode.setTextColor(Color.parseColor("#000000"))
//            binding.tvData1.setTextColor(Color.parseColor("#000000"))
//            binding.tvData2.setTextColor(Color.parseColor("#000000"))
//
//
//        }
//
//
//
//
//        binding.edtData1.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE ||
//                actionId == EditorInfo.IME_ACTION_NEXT ||
//                actionId == EditorInfo.IME_ACTION_SEARCH
//            ) {
//
//
//                if (savedFile == null) {
//                    Toast.makeText(this, "Please select CSV file", Toast.LENGTH_SHORT).show()
//                }  else {
//                    readCsvFile(savedFile!!, 2, binding.edtData1.text.toString())                }
//                true // Return true to indicate the event is handled
//            } else {
//                false
//            }
//        }
//
//
//
//        binding.edtData2.setOnEditorActionListener { v, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE ||
//                actionId == EditorInfo.IME_ACTION_NEXT ||
//                actionId == EditorInfo.IME_ACTION_SEARCH
//            ) {
//
//                if (savedFile == null) {
//                    Toast.makeText(this, "Please select CSV file", Toast.LENGTH_SHORT).show()
//                } else {
//                    readCsvFile(savedFile!!, 3, binding.edtData2.text.toString())
//                }
//                true // Return true to indicate the event is handled
//            } else {
//                false
//            }
//        }
//
//
////        binding.edtData2.doAfterTextChanged {
////            if (savedFile == null) {
////                Toast.makeText(this, "Please select CSV file", Toast.LENGTH_SHORT).show()
////            } else if (binding.edtScanQrHere.text.toString()
////                    .isNotEmpty() && binding.edtData1.text.toString().isNotEmpty()
////            ) {
////                readCsvFile(savedFile!!, 3, it.toString())
////            } else if (binding.edtScanQrHere.text.toString().isEmpty()) {
////                Toast.makeText(this, "Please enter barcode first", Toast.LENGTH_SHORT).show()
////            } else if (binding.edtData1.text.toString().isEmpty()) {
////                Toast.makeText(this, "Please enter data 1", Toast.LENGTH_SHORT).show()
////            }
////        }
//
//
//    }
//
//
//    fun openFile() {
//        val folderPath = Environment.getExternalStorageDirectory().absolutePath + "/input"
//
//// Create a File object for the folder
//        val folder = File(folderPath)
//
//        Log.e("file path", folder.path)
//        // /storage/emulated/0/input
//
//
//// Create a list of CSV files in the folder
//        val csvFiles = folder.listFiles { file -> file.extension.equals("csv", ignoreCase = true) }
//
//        if (csvFiles != null && csvFiles.isNotEmpty()) {
//            // Show CSV files in a ListView or RecyclerView to allow user to pick
//            // For example, use a RecyclerView or show them in a dialog
//            showCsvFilesInPicker(csvFiles)
//        } else {
//            // No CSV files found
//            Toast.makeText(this, "No CSV files found", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    // Open file picker to select a CSV file
//    private fun openFilePicker() {
//
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "text/*" // This allows selecting CSV files (text-based)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        startActivityForResult(Intent.createChooser(intent, "Select CSV File"), PICK_CSV_REQUEST)
//    }
//
//    fun openFolder() {
//
//        val path = Environment.getExternalStorageDirectory().toString() + "/" + "Downloads" + "/"
//        val uri = Uri.parse(path)
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
//        intent.setDataAndType(uri, "*/*")
//        startActivity(intent)
//    }
//
//
//    // Handle file selection result
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == PICK_CSV_REQUEST && resultCode == Activity.RESULT_OK) {
//            data?.data?.let { uri ->
//                //  saveCsvFile(uri)
//            } ?: Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    // Read CSV file and print content in Logcat
//    fun readCsvFile(file: File, columnIndex: Int, text: String) {
//
//        if (columnIndex == 1) {
//
//            Log.e("barcode length ", barcodeData.size.toString())
//
//
//            if (barcodeData.isNotEmpty()) {
//                Log.e("first", barcodeData[0].barcode1)
//            }
//
//
//            for (i in barcodeData.indices) {
//                if (barcodeData[i].barcode1 == text) {
//                    binding.clMain.setBackgroundColor(Color.parseColor("#FF0000"))
//                    binding.mainScroll.setBackgroundColor(Color.parseColor("#FF0000"))
//                    binding.tvResult.setTextColor(Color.parseColor("#FFFFFF"))
//                    binding.tvScannedNumber.setTextColor(Color.parseColor("#FFFFFF"))
//                    binding.tvBarcode.setTextColor(Color.parseColor("#FFFFFF"))
//                    binding.tvData1.setTextColor(Color.parseColor("#FFFFFF"))
//                    binding.tvData2.setTextColor(Color.parseColor("#FFFFFF"))
//                    binding.tvResult.text = "$text is already scanned!"
//                    mediaPlayerFail.start()
//                    hideKeyboard()
//                    return
//                }
//            }
//
//        }
//
//        try {
//            val reader = BufferedReader(FileReader(file))
//            var line: String?
//            var isFirstRow = true
//            var rowIndex = -1
//            var matchedPosition: Int? = null
//            var isMatched = false
//
//            while (reader.readLine().also { line = it } != null) {
//                val columns = line?.split(",") ?: listOf()
//
//                if (columns.isNotEmpty()) {
//                    val columnData = columns[columnIndex].trim().lowercase()
//                    Log.e("columnData", columnData)
////                    if (isFirstRow) {
////                        isFirstRow = false
////                        rowIndex++
////                        continue
////                    }
//
//                    Log.e("text", text)
//                    if (text.trim().lowercase() == columnData && columnIndex == 1) {
//                        matchedRowPosition = rowIndex // Store the row index
//                        binding.tvResult.text = "Match Found!"
//
//                        mediaPlayerSuccess.start()
//
//                        binding.edtData1.requestFocus()
//
//                        binding.clMain.setBackgroundColor(Color.parseColor("#008000"))
//                        binding.mainScroll.setBackgroundColor(Color.parseColor("#008000"))
//                        binding.tvResult.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvScannedNumber.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvBarcode.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvData1.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvData2.setTextColor(Color.parseColor("#FFFFFF"))
//                        isMatched = true
//
//                        firstColumnMatched = true
//
//                        break // Stop searching after finding the first match
//                    } else if (text.trim()
//                            .lowercase() == columnData && rowIndex == matchedRowPosition && columnIndex != 1
//                    ) {
//                        binding.tvResult.text = "Match Found!"
//                        isMatched = true
//                        mediaPlayerSuccess.start()
//                        if (columnIndex == 2 && !thirdColumnMatched) {
//                            secondColumnMatched = true
//                            binding.edtData2.requestFocus()
//                        } else if (columnIndex == 2 && thirdColumnMatched && firstColumnMatched) {
//                            firstColumnMatched = false
//                            secondColumnMatched = false
//                            thirdColumnMatched = false
//
//                            val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                LocalDate.now()
//                            } else {
//                                TODO("VERSION.SDK_INT < O")
//                            }
//                            val formattedDate =
//                                currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
//
//                            val currentTime = LocalTime.now()
//                            val formattedTime =
//                                currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
//
//                            var lineString =
//                                columns[1] + "," + columns[2] + "," + columns[3] + "," + formattedDate + "," + formattedTime
//
//
//                            var tableRowData = TableRowData(
//                                columns[1], columns[2], columns[3],
//                                formattedTime
//                            )
//
//
//
//                            /// Remove from remaining data list
//                            remainingData.removeIf { it.barcode1 == columns[1] }
//
//                            barcodeData.add(tableRowData)
//                            matchedData.add(lineString) // Add matched row
//                            Log.e("line!!", lineString)
//                            binding.tvScannedNumber.visibility = View.VISIBLE
//                            binding.tvScannedNumber.text =
//                                matchedData.size.toString() + " out of " + totalRows.toString() + "\n\n" + "Last Scanned : " + barcodeData[barcodeData.size - 1].barcode1
//
//                            saveMatchedDataToCsv(matchedData, this)
//
//                            //   binding.btnDownload.visibility = View.VISIBLE
//                            hideKeyboard()
//
//                        } else if (columnIndex == 3 && secondColumnMatched && firstColumnMatched) {
//                            firstColumnMatched = false
//                            secondColumnMatched = false
//                            thirdColumnMatched = false
//
//                            val currentDate = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                                LocalDate.now()
//                            } else {
//                                TODO("VERSION.SDK_INT < O")
//                            }
//                            val formattedDate =
//                                currentDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
//
//                            val currentTime = LocalTime.now()
//                            val formattedTime =
//                                currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
//
//                            var lineString =
//                                columns[1] + "," + columns[2] + "," + columns[3] + "," + formattedDate + "," + formattedTime
//
//                            var tableRowData = TableRowData(
//                                columns[1], columns[2], columns[3],
//                                formattedTime
//                            )
//
//                            /// Remove from remaining data list
//                            remainingData.removeIf { it.barcode1 == columns[1] }
//
//                            barcodeData.add(tableRowData)
//                            matchedData.add(lineString) // Add matched row
//                            Log.e("line!!", lineString)
//                            binding.tvScannedNumber.visibility = View.VISIBLE
//                            binding.tvScannedNumber.text =
//                                matchedData.size.toString() + " out of " + totalRows.toString() + "\n\n" + "Last Scanned " + barcodeData[barcodeData.size - 1].barcode1
//
//                            saveMatchedDataToCsv(matchedData, this)
//
//                            //   binding.btnDownload.visibility = View.VISIBLE
//                            hideKeyboard()
//                        } else if (columnIndex == 3 && !secondColumnMatched) {
//                            thirdColumnMatched = true
//                            binding.edtData1.requestFocus()
//                        }
//
//
//                        binding.clMain.setBackgroundColor(Color.parseColor("#008000"))
//                        binding.mainScroll.setBackgroundColor(Color.parseColor("#008000"))
//                        binding.tvResult.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvScannedNumber.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvBarcode.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvData1.setTextColor(Color.parseColor("#FFFFFF"))
//                        binding.tvData2.setTextColor(Color.parseColor("#FFFFFF"))
//                        break
//                    }
//
//                    rowIndex++
//                }
//            }
//
//
//            if (!isMatched) {
//                binding.tvResult.text = "Input Fields do not match!"
//                mediaPlayerFail.start()
//                if (columnIndex == 1) {
//                    firstColumnMatched = false
//                } else if (columnIndex == 2) {
//                    secondColumnMatched = false
//                } else if (columnIndex == 3) {
//                    thirdColumnMatched = false
//                }
//
//
//                hideKeyboard()
//                binding.clMain.setBackgroundColor(Color.parseColor("#FF0000"))
//                binding.mainScroll.setBackgroundColor(Color.parseColor("#FF0000"))
//                binding.tvResult.setTextColor(Color.parseColor("#FFFFFF"))
//                binding.tvScannedNumber.setTextColor(Color.parseColor("#FFFFFF"))
//                binding.tvBarcode.setTextColor(Color.parseColor("#FFFFFF"))
//                binding.tvData1.setTextColor(Color.parseColor("#FFFFFF"))
//                binding.tvData2.setTextColor(Color.parseColor("#FFFFFF"))
//            }
//
//            reader.close()
//
//        } catch (e: Exception) {
//            Toast.makeText(this, "Error reading CSV file", Toast.LENGTH_SHORT).show()
//            Log.e("CSV_ERROR", "Error reading file", e)
//        }
//    }
//
//
//    /**
//     * Function to hide the keyboard
//     */
//    private fun hideKeyboard() {
//        val inputMethodManager =
//            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        val view = currentFocus
//        view?.let {
//            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//
//
//    /* Save CSV in application default storage*/
//    private fun saveCsvFile(uri: Uri) {
//        try {
//            val inputStream = contentResolver.openInputStream(uri)
//            savedFile = File(filesDir, CSV_FILE_NAME) // Internal storage file
//
//            inputStream?.use { input ->
//                FileOutputStream(savedFile).use { output ->
//                    input.copyTo(output)
//                }
//            }
//
//            binding.btnScanFile.text = savedFile?.name
//
//            Toast.makeText(this, "CSV File Saved Successfully", Toast.LENGTH_SHORT).show()
//        } catch (e: Exception) {
//            Toast.makeText(this, "Error Saving CSV File", Toast.LENGTH_SHORT).show()
//            Log.e("CSV_ERROR", "Error saving file", e)
//        }
//    }
//
//
//    fun getCsvFile(): File? {
//        val file = File(filesDir, CSV_FILE_NAME)
//        return if (file.exists()) file else null
//    }
//
//
////    @RequiresApi(Build.VERSION_CODES.R)
////    fun requestAllFilesAccess() {
////        if (!Environment.isExternalStorageManager()) {
////            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
////            intent.data = Uri.parse("package:" + applicationContext.packageName)
////            startActivity(intent)
////        } else {
////            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
////        }
////    }
//
//
//    fun requestAllFilesAccess() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // Android 11+ (API 30+)
//            if (!Environment.isExternalStorageManager()) {
//                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
//                intent.data = Uri.parse("package:" + applicationContext.packageName)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            // Android 10 (API 29) and below
//            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
//                // Scoped Storage - cannot request MANAGE_EXTERNAL_STORAGE
//                Toast.makeText(
//                    this,
//                    "On Android 10, use MediaStore API to access files",
//                    Toast.LENGTH_LONG
//                ).show()
//            } else {
//                // Android 9 and below - Request READ/WRITE permissions
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
//                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ),
//                    PICK_CSV_REQUEST
//                )
//            }
//        }
//    }
//
//
//    private fun requestStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
//            }
//
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
//            }
//
//
//        }
//    }
//
//
//    fun saveMatchedDataToCsv(matchedData: List<String>, context: Context) {
//        try {
//            // Define download folder path
//            val folder = File(
//                Environment.getExternalStorageDirectory(),
//                "output"
//            )
//
//            // Create folder if it doesn't exist
//            if (!folder.exists()) {
//                folder.mkdirs()
//            }
//
//            // Define file path
//            val file = File(folder, savedFile?.name)
//
//            // Write data to the CSV file
//            val writer = BufferedWriter(FileWriter(file))
//            for (row in matchedData) {
//                writer.write(row)
//                writer.newLine()
//            }
//            writer.close()
//
//            // Notify user
//            Toast.makeText(context, "CSV Saved at: ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//
//            // Refresh media store so the file appears in File Manager
//            MediaScannerConnection.scanFile(
//                context,
//                arrayOf(file.absolutePath),
//                arrayOf("text/csv"),
//                null
//            )
//
//            binding.btnOpenCsv.visibility = View.VISIBLE
//            binding.btnScannedData.visibility = View.VISIBLE
//            binding.btnRemainingData.visibility = View.VISIBLE
//
//        } catch (e: Exception) {
//            Toast.makeText(context, "Error saving CSV file", Toast.LENGTH_SHORT).show()
//            Log.e("CSV_ERROR", "Error saving file", e)
//        }
//    }
//
//
//    fun openCsvFile(context: Context, file: File) {
//        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
//
//        val intent = Intent(Intent.ACTION_VIEW).apply {
//            setDataAndType(uri, "text/csv")
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//
//        try {
//            context.startActivity(Intent.createChooser(intent, "Open CSV with"))
//        } catch (e: ActivityNotFoundException) {
//            // If no app found, try opening in web browser
//            try {
//                val webIntent = Intent(Intent.ACTION_VIEW).apply {
//                    data = Uri.parse(uri.toString()) // Convert file URI to web-readable format
//                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//                }
//                context.startActivity(webIntent)
//            } catch (ex: Exception) {
//                Toast.makeText(
//                    context,
//                    "No app or browser found to open CSV file",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }
//
//
//    fun createFolders() {
//        val inputFolder = File(Environment.getExternalStorageDirectory().absolutePath + "/input")
//
//
//        if (!inputFolder.exists()) {
//            val created = inputFolder.mkdirs()
//            println("ImportPhy folder created: $created")
//        }
//
//    }
//
//
//    fun showCsvFilesInPicker(files: Array<File>) {
//        val fileNames = files.map { it.name }
//
//        // Display the files using a dialog or in a list for the user to select
//        AlertDialog.Builder(this)
//            .setTitle("Select a CSV file")
//            .setItems(fileNames.toTypedArray()) { _, which ->
//                val selectedFile = files[which]
//                // Handle selected file
//                handleFileSelection(selectedFile)
//            }
//            .show()
//    }
//
//    fun handleFileSelection(file: File) {
//
//        savedFile = file
//        binding.btnScanFile.text = "File To Scan : " + file.name
//
//        // Handle the selected file (e.g., read it, show content, etc.)
//        Toast.makeText(this, "Selected: ${file.name}", Toast.LENGTH_SHORT).show()
//        // Add your logic to process the selected CSV file here
//
//        barcodeData.clear()
//        matchedData.clear()
//
//        binding.edtScanQrHere.setText("")
//        binding.edtData1.setText("")
//        binding.edtData2.setText("")
//        binding.tvResult.text = "Validation Message"
////        binding.tvScannedNumber.visibility = View.GONE
////        binding.btnOpenCsv.visibility = View.GONE
////        binding.btnScannedData.visibility = View.GONE
////        binding.btnRemainingData.visibility = View.GONE
//        binding.clMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
//        binding.mainScroll.setBackgroundColor(Color.parseColor("#FFFFFF"))
//        binding.tvResult.setTextColor(Color.parseColor("#000000"))
//        binding.tvScannedNumber.setTextColor(Color.parseColor("#000000"))
//        binding.tvBarcode.setTextColor(Color.parseColor("#000000"))
//        binding.tvData1.setTextColor(Color.parseColor("#000000"))
//        binding.tvData2.setTextColor(Color.parseColor("#000000"))
//
//        getTotalRows(file)
//
//
//    }
//
//
//    fun showTableDialog(context: Context) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.dialog_table)
//
//        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewTable)
//        val btnClose = dialog.findViewById<Button>(R.id.btnClose)
//
//
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = TableAdapter(barcodeData)
//
//        btnClose.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog.show()
//    }
//
//    fun showRemainingTableDialog(context: Context) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.dialog_table)
//
//        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recyclerViewTable)
//        val btnClose = dialog.findViewById<Button>(R.id.btnClose)
//
//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.adapter = RemainingTableAdapter(remainingData)
//
//        btnClose.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//        dialog.show()
//    }
//
//
//    fun getTotalRows(file: File) {
//        try {
//            val reader = BufferedReader(FileReader(file))
//            var line: String?
//            var rowIndex = -1
//            totalRows = 0  // Counter for total rows
//            remainingData.clear()
//            while (reader.readLine().also { line = it } != null) {
//                totalRows++ // Increment for each row
//
//                val columns = line?.split(",") ?: listOf()
//
//                Log.e("columns[1]ewrgwe", columns[1])
//
//                remainingData.add(TableRowData(columns[1], columns[2], columns[3], ""))
//
//            }
//
//            Log.e("Total Rows", totalRows.toString()) // Log the total row count
//            reader.close()
//
//            binding.tvScannedNumber.text =
//                matchedData.size.toString() + " out of " + totalRows.toString() + "\n\n" + "Last Scanned :"
//
//
//
//            val outputFilePath = Environment.getExternalStorageDirectory().absolutePath + "/output/" + file.name
//
//            Log.e("outputFilePath", outputFilePath)
//
//
//            val outputFile = File(outputFilePath)
//
//            /// Check that out put file is available the get the data
//            if(outputFile.exists()){
//                getOutputCSVData(outputFile)
//            }
//
//
//
//        } catch (e: Exception) {
//            Toast.makeText(this, "Error reading CSV file", Toast.LENGTH_SHORT).show()
//            Log.e("CSV_ERROR", "Error reading file", e)
//        }
//
//
//    }
//
//
//    fun getOutputCSVData(file: File) {
//        try {
//            val reader = BufferedReader(FileReader(file))
//            var line: String?
//            var rowIndex = -1
//            barcodeData.clear()
//            matchedData.clear()
//            while (reader.readLine().also { line = it } != null) {
//                val columns = line?.split(",") ?: listOf()
//                var lineString =
//                    columns[0] + "," + columns[1] + "," + columns[2] + "," + columns[3] + "," + columns[4]
//
//                matchedData.add(lineString)
//                barcodeData.add(TableRowData(columns[0],columns[1],columns[2],columns[4]))
//
//                /// Remove from remaining data list
//                remainingData.removeIf { it.barcode1 == columns[0] }
//
//
//
//
//            }
//
//            Log.e("Total Rows", totalRows.toString()) // Log the total row count
//            reader.close()
//
//
//            binding.btnRemainingData.visibility = View.VISIBLE
//            binding.btnScannedData.visibility = View.VISIBLE
//            binding.btnOpenCsv.visibility = View.VISIBLE
//            binding.tvScannedNumber.visibility = View.VISIBLE
//            binding.tvScannedNumber.text =
//                matchedData.size.toString() + " out of " + totalRows.toString() + "\n\n" + "Last Scanned : " + barcodeData[barcodeData.size - 1].barcode1
//
//            binding.tvResult.text = "Validation Message"
//
//        } catch (e: Exception) {
//            Toast.makeText(this, "Error reading CSV file", Toast.LENGTH_SHORT).show()
//            Log.e("CSV_ERROR", "Error reading file", e)
//        }
//
//
//    }
//
//
//
//}