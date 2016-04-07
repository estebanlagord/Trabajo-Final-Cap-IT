package com.javier.lagord.trabajofinalcapit.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.javier.lagord.trabajofinalcapit.model.Curso;



public class DatabaseHelper extends SQLiteOpenHelper 
{	
	private final static String DATABASE_NAME = "CapITDatabase";
	private final static int DATABASE_VERSION = 5;

	// Users table
	private final static String TABLE_USERS = "Users";
	private final static String COLUMN_USERS_NAME = "Name";
    private final static String COLUMN_USERS_USERNAME = "Username";
    private final static String COLUMN_USERS_PASSWORD = "Password";
    private final static String DROP_TABLE_USERS = "DROP TABLE IF EXISTS " + TABLE_USERS;
    private final static String CREATE_TABLE_USERS = "CREATE TABLE "+ TABLE_USERS 
    		+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ COLUMN_USERS_USERNAME + " TEXT collate nocase, " // The username will be case insensitive
    		+ COLUMN_USERS_PASSWORD + " TEXT, "
    		+ COLUMN_USERS_NAME+" TEXT)";
    
    // Enrollments table
    private final static String TABLE_ENROLLMENTS = "Enrollments";
    private final static String COLUMN_ENROLLMENTS_USERNAME = "Username";
    private final static String COLUMN_ENROLLMENTS_COURSE = "Course";
    private final static String DROP_TABLE_ENROLLMENTS = "DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS;
    private final static String CREATE_TABLE_ENROLLMENTS = "CREATE TABLE "+ TABLE_ENROLLMENTS 
    		+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ COLUMN_ENROLLMENTS_USERNAME + " TEXT collate nocase, " // The username will be case insensitive
    		+ COLUMN_ENROLLMENTS_COURSE + " TEXT)";
	
    // Courses table
    private final static String TABLE_COURSES = "Courses";
    private final static String COLUMN_COURSES_NAME = "Name";
    private final static String COLUMN_COURSES_IMAGE = "Image";
    private final static String COLUMN_COURSES_DESCRIPTION = "Description";
    private final static String COLUMN_COURSES_NUMBER_OF_HOURS = "NumberOfHours";
    private final static String COLUMN_COURSES_START = "Start";
    private final static String COLUMN_COURSES_MODALIDAD = "Modalidad";
    private final static String COLUMN_COURSES_DATES_TIMES = "DatesTimes";
    private final static String DROP_TABLE_COURSES = "DROP TABLE IF EXISTS " + TABLE_COURSES;
    private final static String CREATE_TABLE_COURSES = "CREATE TABLE "+ TABLE_COURSES 
    		+"(id INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ COLUMN_COURSES_NAME + " TEXT collate nocase, " // The name will be case insensitive
    		+ COLUMN_COURSES_IMAGE + " TEXT, "
    		+ COLUMN_COURSES_DESCRIPTION + " TEXT, "
    		+ COLUMN_COURSES_NUMBER_OF_HOURS + " TEXT, "
    		+ COLUMN_COURSES_START + " TEXT, "
    		+ COLUMN_COURSES_MODALIDAD + " TEXT, "
    		+ COLUMN_COURSES_DATES_TIMES + " TEXT)";
    
    
    private final static String DATABASE_DROP_STATEMENT = "DROP TABLE IF EXISTS " + DATABASE_NAME;
    
    private Context context;
    private static DatabaseHelper databaseInstance;
    
    
    public static DatabaseHelper getDatabaseInstance(Context context)
    {
    	if(databaseInstance == null)
    	{
    		databaseInstance = new DatabaseHelper(context.getApplicationContext());
    	}
    	return databaseInstance;
    }
    
    private DatabaseHelper(Context context, String name, CursorFactory factory, int version) 
    {
        super(context, name, factory, version);      
        this.context = context;
    }
    
    private DatabaseHelper(Context context)
    {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);    	
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) 
    {       	
    	db.execSQL(CREATE_TABLE_USERS);
    	db.execSQL(CREATE_TABLE_ENROLLMENTS);
    	db.execSQL(CREATE_TABLE_COURSES);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
    {      
        db.execSQL(DATABASE_DROP_STATEMENT);
        db.execSQL(DROP_TABLE_COURSES);
        db.execSQL(DROP_TABLE_ENROLLMENTS);
        db.execSQL(DROP_TABLE_USERS);
        db.execSQL(CREATE_TABLE_USERS);
    	db.execSQL(CREATE_TABLE_ENROLLMENTS);
    	db.execSQL(CREATE_TABLE_COURSES);
    }    
    
    public Boolean databaseExists()
    {
    	if(SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getPath(), null, 0) != null)
    	{
    		this.close();
    		return true;
    	}
    	
    	return false;    			
    }
    
    
    public boolean isUsernameInDB(String username){
    	String[] column = new String[]{COLUMN_USERS_USERNAME};
    	String[] arg = new String[]{username};
    	
    	Cursor cursor = getReadableDatabase().query(TABLE_USERS, column, COLUMN_USERS_USERNAME + "=?", arg, null, null, null);
    	boolean foundUser =  cursor.moveToFirst();
    	cursor.close();
    	this.close();
    	return foundUser;
    }

    
    /**
     * Checks if the username/password are valid to login to the application
     * @param username
     * @param password
     * @return The name of the person associated to the username 
     * @throws IllegalArgumentException When one of the arguments is empty, 
     * 			or when the username does not exist,
     * 			or when the password does not match the one from the DB
     */
    public String login(String username, String password) throws IllegalArgumentException{
    	if (username == null || password == null || username.length() == 0 || password.length() == 0)
    		throw new IllegalArgumentException("Tenes que ingresar Usuario y Contraseña");
    	
    	String[] column = new String[]{COLUMN_USERS_NAME, COLUMN_USERS_PASSWORD};
    	String[] arg = new String[]{username};
    	
    	Cursor cursor = getReadableDatabase().query(TABLE_USERS, column, COLUMN_USERS_USERNAME + "=?", arg, null, null, null);
    	boolean foundUser = cursor.moveToFirst();
    	
    	if (!foundUser)
    		throw new IllegalArgumentException("El nombre de usuario no existe: " + username);
    	
    	String savedPassword = cursor.getString(cursor.getColumnIndex(COLUMN_USERS_PASSWORD));
    	if (!savedPassword.equals(password))
    		throw new IllegalArgumentException("Contraseña incorrecta");
    	
    	String savedName = cursor.getString(cursor.getColumnIndex(COLUMN_USERS_NAME));
    	cursor.close();
    	this.close();
    	return savedName;
    }
    
    /**
     * Creates a new account in the DB
     * @param name The person's name
     * @param username
     * @param password
     * @throws IllegalArgumentException When one of the parameters is empty or the username already exist
     * @throws SQLException When there was a problem adding the user to the database
     */
    public void createNewAccount(String name, String username,	String password) throws SQLException, IllegalArgumentException{
    	if (name == null || username == null || password == null || name.length() == 0 || username.length() == 0 || password.length() == 0)
    		throw new IllegalArgumentException("Tenes que completar todos los campos");
    	if (isUsernameInDB(username))
    		throw new IllegalArgumentException("El nombre de usuario ya existe: " + username);
    	
    	ContentValues values = new ContentValues();
    	values.put(COLUMN_USERS_NAME, name);
    	values.put(COLUMN_USERS_USERNAME, username);
    	values.put(COLUMN_USERS_PASSWORD, password);
    	
    	long result = getWritableDatabase().insert(TABLE_USERS, null, values);
    	this.close();
    	
    	if (result < 0)
    		throw new SQLException("No se pudo agregar el usuario a la base de datos");
    }
    
    
    public void enrollUserToCourse(String username, String course) throws SQLException{
    	if (!isUserEnrolledInCourse(username, course)){
        	ContentValues values = new ContentValues();
        	values.put(COLUMN_ENROLLMENTS_USERNAME, username);
        	values.put(COLUMN_ENROLLMENTS_COURSE, course);
        	
        	long result = getWritableDatabase().insert(TABLE_ENROLLMENTS, null, values);
        	this.close();
        	
        	if (result < 0)
        		throw new SQLException("No se pudo agregar el usuario a la base de datos");
    	}
    }
    
    
    public void unenrollUserToCourse(String username, String course){
    		String[] args = new String[]{username, course};
    		getWritableDatabase().delete(TABLE_ENROLLMENTS, 
    				COLUMN_ENROLLMENTS_USERNAME + " = ? and " + COLUMN_ENROLLMENTS_COURSE + " = ?", args);
    		
    		this.close();
    }
    
    
    /**
     * Updates all the courses stored in the database with an updated list of courses
     */
    public void updateAllCourses(List<Curso> courses) throws SQLException{
    	boolean success = true;
    	
    	// delete all old courses
    	getWritableDatabase().delete(TABLE_COURSES, null, null);
    	
    	// insert all new ones
    	for (Curso cur : courses){
    		ContentValues values = new ContentValues();
        	values.put(COLUMN_COURSES_NAME, cur.getNombre());
        	values.put(COLUMN_COURSES_IMAGE, cur.getImagen());
        	values.put(COLUMN_COURSES_DESCRIPTION, cur.getDescripcion());
        	values.put(COLUMN_COURSES_NUMBER_OF_HOURS, cur.getCarga_horaria());
        	values.put(COLUMN_COURSES_START, cur.getComienzo());
        	values.put(COLUMN_COURSES_MODALIDAD, cur.getModalidad());
        	values.put(COLUMN_COURSES_DATES_TIMES, cur.getDias_horarios());
        	
        	long result = getWritableDatabase().insert(TABLE_COURSES, null, values);
        	
        	if (result < 0) {
        		success = false;
        		break;
        	}
    	}
    	
    	this.close();
    	
    	if (!success)
    		throw new SQLException("No se pudo agregar el usuario a la base de datos");
    }
    
    
    public List<Curso> getAllCourses(){
    	List<Curso> result = new ArrayList<Curso>();
    	
    	Cursor cursor = getReadableDatabase().query(TABLE_COURSES, null, null, null, null, null, null);
    	while(cursor.moveToNext()){
    		Curso cur = new Curso(
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_NAME)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_IMAGE)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_DESCRIPTION)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_NUMBER_OF_HOURS)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_START)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_MODALIDAD)),
    				cursor.getString(cursor.getColumnIndex(COLUMN_COURSES_DATES_TIMES))
    				);
    		result.add(cur);
    	}
    	Collections.sort(result);
    	
    	cursor.close();
    	this.close();
    	return result;
    }
    
    /**
     * Gets all the courses for which username is enrolled
     */
    public List<Curso> getCoursesForUser(String username){
    	List<String> coursesForUser = new ArrayList<String>();
    	List<Curso> result = new ArrayList<Curso>();
    	
    	String[] column = new String[]{COLUMN_ENROLLMENTS_COURSE};
    	String[] arg = new String[]{username};
    	
    	Cursor cursor = getReadableDatabase().query(TABLE_ENROLLMENTS, column, COLUMN_ENROLLMENTS_USERNAME + "=?", arg, null, null, null);
    	while (cursor.moveToNext()){
    		String courseName = cursor.getString(cursor.getColumnIndex(COLUMN_ENROLLMENTS_COURSE));
   			coursesForUser.add(courseName);
    	}
    	
    	for (Curso cur : getAllCourses()){
    		if (coursesForUser.contains(cur.getNombre()))
    			result.add(cur);
    	}
    	Collections.sort(result);
    	
    	cursor.close();
    	this.close();
    	return result;
    }
    
    
    public boolean isUserEnrolledInCourse(String username, String courseName){
    	String[] args = new String[]{username, courseName};
    	
    	Cursor cursor = getReadableDatabase().query(TABLE_ENROLLMENTS, null, COLUMN_ENROLLMENTS_USERNAME + "=? AND " + COLUMN_ENROLLMENTS_COURSE + "=?", args, null, null, null);
    	boolean isEnrolled = cursor.moveToFirst();
    	
    	cursor.close();
    	this.close();
    	return isEnrolled;
    }
}
