package folder.android.data;

import android.Manifest;
import android.animation.*;
import android.app.*;
import android.content.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.net.*;
import android.net.Uri;
import android.os.*;
import android.text.*;
import android.text.style.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.animation.*;
import android.webkit.*;
import android.widget.*;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.appbar.AppBarLayout;
import files.permission11.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import org.json.*;
import androidx.documentfile.provider.DocumentFile;
import android.provider.DocumentsContract;
import android.provider.DocumentsContract.Document;
import android.database.*;
import java.io.*;

@np.annotation.NPProtect
public class MainActivity extends AppCompatActivity {
	
	private Toolbar _toolbar;
	private AppBarLayout _app_bar;
	private CoordinatorLayout _coordinator;
	private double PermissionNumber = 0;
	private String str = "";
	private String currentLoc = "";
	private String lastLoc = "";
	private HashMap<String, Object> map = new HashMap<>();
	private double n = 0;
	private double index = 0;
	private String path_uri = "";
	
	private ArrayList<HashMap<String, Object>> listmap = new ArrayList<>();
	private ArrayList<String> list = new ArrayList<>();
	private ArrayList<String> folderList = new ArrayList<>();
	private ArrayList<String> fileList = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> listmap_new = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> folder_List = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> file_List = new ArrayList<>();
	
	private LinearLayout linear1;
	private HorizontalScrollView hscroll1;
	private ListView listview1;
	private LinearLayout linear3;
	private TextView textview1;
	
	private Intent i = new Intent();
	
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
		|| ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
			ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
		} else {
			initializeLogic();
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == 1000) {
			initializeLogic();
		}
	}
	
	private void initialize(Bundle _savedInstanceState) {
		_app_bar = findViewById(R.id._app_bar);
		_coordinator = findViewById(R.id._coordinator);
		_toolbar = findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				onBackPressed();
			}
		});
		linear1 = findViewById(R.id.linear1);
		hscroll1 = findViewById(R.id.hscroll1);
		listview1 = findViewById(R.id.listview1);
		linear3 = findViewById(R.id.linear3);
		textview1 = findViewById(R.id.textview1);
	}
	
	private void initializeLogic() {
		_pershp = getSharedPreferences("fileSp", Context.MODE_PRIVATE);
		permiss11 = new Permisson11(_pershp,MainActivity.this).loadData();
		permiss11.requestPermissionAllFilesAccess();
		// ibino.ir
		
		currentLoc = FileUtil.getExternalStorageDir();
		_setStorage(FileUtil.getExternalStorageDir(), false);
	}
	
	@Override
	protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
		super.onActivityResult(_requestCode, _resultCode, _data);
		    if (_resultCode == Activity.RESULT_OK) {
				            if (_data != null) {
						        permiss11.persistFolder(_data);
						       muri = _data.getData();
						if (Uri.decode(muri.toString()).endsWith(":")) {
								SketchwareUtil.showMessage(getApplicationContext(), "خطا ، نمیتوانید این پوشه را انتخاب کنید");
						}
						else {
								try {
										final int takeFlags = i.getFlags()
										            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
										            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
										// Check for the freshest data.
										getContentResolver().takePersistableUriPermission(muri, takeFlags);
										    mfile = DocumentFile.fromTreeUri(this, muri);
										                    
										    mfile1 = mfile.createFile("*/*", "test.file");
										    uri2 = mfile1.getUri();
										try{
												        DocumentsContract.deleteDocument(getApplicationContext().getContentResolver(), uri2);
												     
												        } catch (FileNotFoundException e) {
												         
												    }             
										parenturi = Uri.parse(pathToRealUri(path_uri));
										if (updateDirectoryEntries(parenturi)) {
												_setListCustomViewData();
										}
								} catch (Exception e) {
										SketchwareUtil.showMessage(getApplicationContext(), "خطا ، نمیتوان پوشه را انتخاب کرد");
										i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
										i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
										muri = Uri.parse(pathToRealUri(path_uri));
										    i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
										        startActivityForResult(i, NEW_FOLDER_REQUEST_CODE);
								}
						}
						       } else {
						        
						   }
				       } else {
				       currentLoc = path_uri.substring((int)(0), (int)(path_uri.lastIndexOf("/")));
				_setStorage(path_uri.substring((int)(0), (int)(path_uri.lastIndexOf("/"))), false);
				SketchwareUtil.showMessage(getApplicationContext(), "اجازه ندادی🤕");
				   }
		switch (_requestCode) {
			
			default:
			break;
		}
	}
	
	
	@Override
	public void onBackPressed() {
		if (currentLoc.equals(FileUtil.getExternalStorageDir())) {
			finish();
		}
		else {
			if (currentLoc.startsWith("primary:".concat(path_uri.replace(FileUtil.getExternalStorageDir().concat("/"), "")))) {
				if (!currentLoc.equals("primary:".concat(path_uri.replace(FileUtil.getExternalStorageDir().concat("/"), "")))) {
					listmap.clear();
					currentLoc = currentLoc.substring((int)(0), (int)(currentLoc.lastIndexOf("/")));
					listmap.clear();
					if (updateDirectoryEntries(parenturi, currentLoc)) {
						_setListCustomViewData();
					}
				}
				else {
					currentLoc = path_uri.substring((int)(0), (int)(path_uri.lastIndexOf("/")));
					_setStorage(path_uri.substring((int)(0), (int)(path_uri.lastIndexOf("/"))), false);
				}
			}
			else {
				if (currentLoc.startsWith(FileUtil.getExternalStorageDir())) {
					currentLoc = currentLoc.substring((int)(0), (int)(currentLoc.lastIndexOf("/")));
					_setStorage(currentLoc, false);
				}
			}
		}
	}
	public void _setStorage(final String _path, final boolean _permission) {
		textview1.setText(_path);
		list.clear();
		listmap.clear();
		folderList.clear();
		fileList.clear();
		if (_permission) {
			path_uri = _path;
			try {
				muri = Uri.parse(pathToUri(path_uri));
				    mfile = DocumentFile.fromTreeUri(this, muri);
				                    
				if (!mfile.canRead() || !mfile.canWrite()) {
					i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
					i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
					muri = Uri.parse(pathToUri(path_uri));
					    i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
					        startActivityForResult(i, NEW_FOLDER_REQUEST_CODE);
				}
				else {
					parenturi = Uri.parse(pathToRealUri(path_uri));
					if (updateDirectoryEntries(parenturi)) {
						_setListCustomViewData();
					}
				}
			} catch (Exception e) {
				i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
				i.setAction(Intent.ACTION_OPEN_DOCUMENT_TREE);
				muri = Uri.parse(pathToUri(path_uri));
				    i.putExtra(DocumentsContract.EXTRA_INITIAL_URI, muri);
				        startActivityForResult(i, NEW_FOLDER_REQUEST_CODE);
			}
		}
		else {
			FileUtil.listDir(_path, list);
			Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
			index = 0;
			for(int _repeat52 = 0; _repeat52 < (int)(list.size()); _repeat52++) {
				if (FileUtil.isDirectory(list.get((int)(index)))) {
					folderList.add(list.get((int)(index)));
				}
				else {
					fileList.add(list.get((int)(index)));
				}
				index++;
			}
			index = 0;
			for(int _repeat66 = 0; _repeat66 < (int)(folderList.size()); _repeat66++) {
				map = new HashMap<>();
				map.put("name", Uri.parse(folderList.get((int)(index))).getLastPathSegment());
				map.put("docId", folderList.get((int)(index)));
				map.put("mime", "");
				listmap.add(map);
				index++;
			}
			index = 0;
			for(int _repeat80 = 0; _repeat80 < (int)(fileList.size()); _repeat80++) {
				map = new HashMap<>();
				map.put("name", Uri.parse(fileList.get((int)(index))).getLastPathSegment());
				map.put("docId", fileList.get((int)(index)));
				map.put("mime", "");
				listmap.add(map);
				index++;
			}
			listview1.setAdapter(new Listview1Adapter(listmap));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
	}
	Boolean updateDirectoryEntries(Uri uri) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		
		        
		ContentResolver contentResolver = getApplicationContext().getContentResolver();
		
		
		        Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri,
		                DocumentsContract.getTreeDocumentId(uri));
		
		 
		
		        Cursor childCursor = contentResolver.query(childrenUri, new String[]{Document.COLUMN_DOCUMENT_ID,
			                Document.COLUMN_DISPLAY_NAME, Document.COLUMN_MIME_TYPE}, null, null, null);
		        try {
			            
			            while (childCursor.moveToNext()) {
				                
				
				
				final String docId = childCursor.getString(0);
									                final String name = childCursor.getString(1);
									                final String mime = childCursor.getString(2);
				
				
				map = new HashMap<>();
									                
									map.put("docId", docId);
									map.put("name", name);
									map.put("mime", mime);
						listmap.add(map);
						            }
				            
			        } finally {
			            closeQuietly(childCursor);
			        }
		return true;
		    }
	
	
	
	
	
	
	// SECOND METHOD TO GET ENTRIES FOR CHILDREN
	
	Boolean updateDirectoryEntries(Uri uri, String str) {
		
		HashMap<String, Object> map = new HashMap<>();
		
		 
		        
		ContentResolver contentResolver = getApplicationContext().getContentResolver();
		
		
		        Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, str);
		
		 
		
		        Cursor childCursor = contentResolver.query(childrenUri, new String[]{Document.COLUMN_DOCUMENT_ID,
			                Document.COLUMN_DISPLAY_NAME, Document.COLUMN_MIME_TYPE}, null, null, null);
		        try {
			            
			            while (childCursor.moveToNext()) {
				                
				
				
				final String docId = childCursor.getString(0);
									                final String name = childCursor.getString(1);
									                final String mime = childCursor.getString(2);
				
				
				map = new HashMap<>();
									                
									map.put("docId", docId);
									map.put("name", name);
									map.put("mime", mime);
						listmap.add(map);
						            }
				            
			        } finally {
			            closeQuietly(childCursor);
			        }
		return true;
		    }
	
	
	
	// Util method to check if the mime type is a directory
	private static boolean isDirectory(String mimeType) {
		    return DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType);
	}
	
	
	// Util method to close a closeable
	private static void closeQuietly(Closeable closeable) {
		    if (closeable != null) {
			        try {
				            closeable.close();
				        } catch (RuntimeException re) {
				            throw re;
				        } catch (Exception ignore) {
				            // ignore exception
				        }
			    }
	}
	public String pathToUri( String _path) {
				uriFor1 = "content://com.android.externalstorage.documents/tree/primary%3A";
				uriFor3 = "/document/primary%3A";
		
		if ( _path.endsWith("/")) {
			_path = _path.substring(0, _path.length()-1);
		}
		
				if (_path.contains("/sdcard/")) {
						uriFor2 = _path.replace("/sdcard/", "").replace("/", "%2F");
						
						if (uriFor2.substring(uriFor2.length()-1, uriFor2.length()).equals("/")) {
								
								uriFor2 = uriFor1.substring(0, uriFor1.length()-1);
								
						}
						
						
				}
				else {
						if (_path.contains("/storage/") && _path.contains("/emulated/")) {
								uriFor2 = _path.replace("/storage/emulated/0/", "").replace("/", "%2F");
								
								if (uriFor2.substring(uriFor2.length()-1, uriFor2.length()).equals("/")) {
										
										uriFor2 = uriFor1.substring(0, uriFor1.length()-1);
										
								}
								
						}
						else {
								
						}
				}
				return uriFor1 = uriFor1 + uriFor2 + uriFor3 + uriFor2;
		}
	public String pathToRealUri( String _path) {
				uriFor1 = "content://com.android.externalstorage.documents/tree/primary%3A";
		
		if ( _path.endsWith("/")) {
			_path = _path.substring(0, _path.length()-1);
		}
		
		
				if (_path.contains("/sdcard/")) {
						uriFor2 = _path.replace("/sdcard/", "").replace("/", "%2F");
						
						if (uriFor2.substring(uriFor2.length()-1, uriFor2.length()).equals("/")) {
								
								uriFor2 = uriFor1.substring(0, uriFor1.length()-1);
								
						}
						
				}
				else {
						if (_path.contains("/storage/") && _path.contains("/emulated/")) {
								uriFor2 = _path.replace("/storage/emulated/0/", "").replace("/", "%2F");
								
								if (uriFor2.substring(uriFor2.length()-1, uriFor2.length()).equals("/")) {
										
										uriFor2 = uriFor1.substring(0, uriFor1.length()-1);
										
								}	
								
						}
						else {
								
						}
				}
				return uriFor1 = uriFor1 + uriFor2;
		}
	private String uriFor1 = "";
	private String uriFor2 = "";
	private String uriFor3 = "";
	private  Uri muri;
	private  static final int NEW_FOLDER_REQUEST_CODE = 43;
	private  Uri uri2;
	private  Uri parenturi ;
	private  DocumentFile mfile;
	private  DocumentFile mfile1;
	{
	}
	
	
	public void _setListCustomViewData() {
		textview1.setText(currentLoc.replace("primary:", FileUtil.getExternalStorageDir().concat("/")));
		try {
			folder_List.clear();
			file_List.clear();
			listmap_new.clear();
			index = 0;
			for(int _repeat14 = 0; _repeat14 < (int)(listmap.size()); _repeat14++) {
				if (isDirectory(listmap.get((int)index).get("mime").toString())) {
					map = new HashMap<>();
					map.put("name", listmap.get((int)index).get("name").toString());
					map.put("docId", listmap.get((int)index).get("docId").toString());
					map.put("mime", listmap.get((int)index).get("mime").toString());
					folder_List.add(map);
				}
				else {
					map = new HashMap<>();
					map.put("name", listmap.get((int)index).get("name").toString());
					map.put("docId", listmap.get((int)index).get("docId").toString());
					map.put("mime", listmap.get((int)index).get("mime").toString());
					file_List.add(map);
				}
				index++;
			}
			index = 0;
			for(int _repeat44 = 0; _repeat44 < (int)(folder_List.size()); _repeat44++) {
				map = new HashMap<>();
				map.put("name", folder_List.get((int)index).get("name").toString());
				map.put("docId", folder_List.get((int)index).get("docId").toString());
				map.put("mime", folder_List.get((int)index).get("mime").toString());
				listmap_new.add(map);
				index++;
			}
			index = 0;
			for(int _repeat59 = 0; _repeat59 < (int)(file_List.size()); _repeat59++) {
				map = new HashMap<>();
				map.put("name", file_List.get((int)index).get("name").toString());
				map.put("docId", file_List.get((int)index).get("docId").toString());
				map.put("mime", file_List.get((int)index).get("mime").toString());
				listmap_new.add(map);
				index++;
			}
			listview1.setAdapter(new Listview1Adapter(listmap_new));
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		} catch (Exception e) {
			SketchwareUtil.showMessage(getApplicationContext(), "خطا");
		}
	}
	
	
	public void _permission11() {
	}
	
	private Permisson11 permiss11;
	private SharedPreferences _pershp;
	
	{
		
		
		
		
		
	}
	
	public class Listview1Adapter extends BaseAdapter {
		
		ArrayList<HashMap<String, Object>> _data;
		
		public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
			_data = _arr;
		}
		
		@Override
		public int getCount() {
			return _data.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int _index) {
			return _data.get(_index);
		}
		
		@Override
		public long getItemId(int _index) {
			return _index;
		}
		
		@Override
		public View getView(final int _position, View _v, ViewGroup _container) {
			LayoutInflater _inflater = getLayoutInflater();
			View _view = _v;
			if (_view == null) {
				_view = _inflater.inflate(R.layout.files_c, null);
			}
			
			final LinearLayout linear2 = _view.findViewById(R.id.linear2);
			final ImageView imageview1 = _view.findViewById(R.id.imageview1);
			final LinearLayout linear3 = _view.findViewById(R.id.linear3);
			final TextView textview1 = _view.findViewById(R.id.textview1);
			
			try {
				textview1.setText(_data.get((int)_position).get("name").toString());
			} catch (Exception e) {
				 
			}
			if (_data.get((int)_position).get("docId").toString().startsWith("primary:Android/data/") || _data.get((int)_position).get("docId").toString().startsWith("primary:Android/obb/")) {
				if (isDirectory(_data.get((int)_position).get("mime").toString())) {
					imageview1.setImageResource(R.drawable.folder);
					linear2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
							currentLoc = _data.get((int)_position).get("docId").toString();
							listmap.clear();
							if (updateDirectoryEntries(parenturi, currentLoc)) {
								_setListCustomViewData();
							}
						}
					});
				}
				else {
					imageview1.setImageResource(R.drawable.file);
					linear2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
							SketchwareUtil.showMessage(getApplicationContext(), "فایل");
						}
					});
				}
			}
			else {
				if (FileUtil.isDirectory(_data.get((int)_position).get("docId").toString())) {
					imageview1.setImageResource(R.drawable.folder);
					if (_data.get((int)_position).get("docId").toString().equals(FileUtil.getExternalStorageDir().concat("/Android/data"))) {
						linear2.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View _view) {
								currentLoc = "primary:Android/data";
								_setStorage(_data.get((int)_position).get("docId").toString(), true);
							}
						});
					}
					else {
						if (_data.get((int)_position).get("docId").toString().equals(FileUtil.getExternalStorageDir().concat("/Android/obb"))) {
							linear2.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View _view) {
									currentLoc = "primary:Android/obb";
									_setStorage(_data.get((int)_position).get("docId").toString(), true);
								}
							});
						}
						else {
							linear2.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View _view) {
									currentLoc = _data.get((int)_position).get("docId").toString();
									_setStorage(_data.get((int)_position).get("docId").toString(), false);
								}
							});
						}
					}
				}
				else {
					imageview1.setImageResource(R.drawable.file);
					linear2.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View _view) {
							SketchwareUtil.showMessage(getApplicationContext(), "فایل");
						}
					});
				}
			}
			
			return _view;
		}
	}
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels() {
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels() {
		return getResources().getDisplayMetrics().heightPixels;
	}
}
