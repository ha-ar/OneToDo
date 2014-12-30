package com.vector.onetodo.db.gen;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.vector.onetodo.db.gen.ToDo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table TO_DO.
*/
public class ToDoDao extends AbstractDao<ToDo, Long> {

    public static final String TABLENAME = "TO_DO";

    /**
     * Properties of entity ToDo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property User_id = new Property(1, Integer.class, "user_id", false, "USER_ID");
        public final static Property Todo_type_id = new Property(2, Integer.class, "todo_type_id", false, "TODO_TYPE_ID");
        public final static Property Parent = new Property(3, Integer.class, "parent", false, "PARENT");
        public final static Property Title = new Property(4, String.class, "title", false, "TITLE");
        public final static Property Is_allday = new Property(5, Boolean.class, "is_allday", false, "IS_ALLDAY");
        public final static Property Start_date = new Property(6, Long.class, "start_date", false, "START_DATE");
        public final static Property End_date = new Property(7, Long.class, "end_date", false, "END_DATE");
        public final static Property Location = new Property(8, String.class, "location", false, "LOCATION");
        public final static Property Priority = new Property(9, Integer.class, "priority", false, "PRIORITY");
        public final static Property Notes = new Property(10, String.class, "notes", false, "NOTES");
        public final static Property Todo_id = new Property(11, long.class, "todo_id", false, "TODO_ID");
    };

    private DaoSession daoSession;


    public ToDoDao(DaoConfig config) {
        super(config);
    }
    
    public ToDoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'TO_DO' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'USER_ID' INTEGER," + // 1: user_id
                "'TODO_TYPE_ID' INTEGER," + // 2: todo_type_id
                "'PARENT' INTEGER," + // 3: parent
                "'TITLE' TEXT," + // 4: title
                "'IS_ALLDAY' INTEGER," + // 5: is_allday
                "'START_DATE' INTEGER," + // 6: start_date
                "'END_DATE' INTEGER," + // 7: end_date
                "'LOCATION' TEXT," + // 8: location
                "'PRIORITY' INTEGER," + // 9: priority
                "'NOTES' TEXT," + // 10: notes
                "'TODO_ID' INTEGER NOT NULL );"); // 11: todo_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'TO_DO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ToDo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(2, user_id);
        }
 
        Integer todo_type_id = entity.getTodo_type_id();
        if (todo_type_id != null) {
            stmt.bindLong(3, todo_type_id);
        }
 
        Integer parent = entity.getParent();
        if (parent != null) {
            stmt.bindLong(4, parent);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(5, title);
        }
 
        Boolean is_allday = entity.getIs_allday();
        if (is_allday != null) {
            stmt.bindLong(6, is_allday ? 1l: 0l);
        }
 
        Long start_date = entity.getStart_date();
        if (start_date != null) {
            stmt.bindLong(7, start_date);
        }
 
        Long end_date = entity.getEnd_date();
        if (end_date != null) {
            stmt.bindLong(8, end_date);
        }
 
        String location = entity.getLocation();
        if (location != null) {
            stmt.bindString(9, location);
        }
 
        Integer priority = entity.getPriority();
        if (priority != null) {
            stmt.bindLong(10, priority);
        }
 
        String notes = entity.getNotes();
        if (notes != null) {
            stmt.bindString(11, notes);
        }
        stmt.bindLong(12, entity.getTodo_id());
    }

    @Override
    protected void attachEntity(ToDo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ToDo readEntity(Cursor cursor, int offset) {
        ToDo entity = new ToDo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // user_id
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // todo_type_id
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // parent
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // title
            cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0, // is_allday
            cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6), // start_date
            cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7), // end_date
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // location
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // priority
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // notes
            cursor.getLong(offset + 11) // todo_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ToDo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUser_id(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setTodo_type_id(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setParent(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setTitle(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIs_allday(cursor.isNull(offset + 5) ? null : cursor.getShort(offset + 5) != 0);
        entity.setStart_date(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
        entity.setEnd_date(cursor.isNull(offset + 7) ? null : cursor.getLong(offset + 7));
        entity.setLocation(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setPriority(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setNotes(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTodo_id(cursor.getLong(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ToDo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ToDo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCheckListDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getAssignDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getShareDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getLabelDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T4", daoSession.getReminderDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T5", daoSession.getRepeatDao().getAllColumns());
            builder.append(" FROM TO_DO T");
            builder.append(" LEFT JOIN CHECK_LIST T0 ON T.'TODO_ID'=T0.'_id'");
            builder.append(" LEFT JOIN ASSIGN T1 ON T.'TODO_ID'=T1.'_id'");
            builder.append(" LEFT JOIN SHARE T2 ON T.'TODO_ID'=T2.'_id'");
            builder.append(" LEFT JOIN LABEL T3 ON T.'TODO_ID'=T3.'_id'");
            builder.append(" LEFT JOIN REMINDER T4 ON T.'TODO_ID'=T4.'_id'");
            builder.append(" LEFT JOIN REPEAT T5 ON T.'TODO_ID'=T5.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ToDo loadCurrentDeep(Cursor cursor, boolean lock) {
        ToDo entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        CheckList checkList = loadCurrentOther(daoSession.getCheckListDao(), cursor, offset);
         if(checkList != null) {
            entity.setCheckList(checkList);
        }
        offset += daoSession.getCheckListDao().getAllColumns().length;

        Assign assign = loadCurrentOther(daoSession.getAssignDao(), cursor, offset);
         if(assign != null) {
            entity.setAssign(assign);
        }
        offset += daoSession.getAssignDao().getAllColumns().length;

        Share share = loadCurrentOther(daoSession.getShareDao(), cursor, offset);
         if(share != null) {
            entity.setShare(share);
        }
        offset += daoSession.getShareDao().getAllColumns().length;

        Label label = loadCurrentOther(daoSession.getLabelDao(), cursor, offset);
         if(label != null) {
            entity.setLabel(label);
        }
        offset += daoSession.getLabelDao().getAllColumns().length;

        Reminder reminder = loadCurrentOther(daoSession.getReminderDao(), cursor, offset);
         if(reminder != null) {
            entity.setReminder(reminder);
        }
        offset += daoSession.getReminderDao().getAllColumns().length;

        Repeat repeat = loadCurrentOther(daoSession.getRepeatDao(), cursor, offset);
         if(repeat != null) {
            entity.setRepeat(repeat);
        }

        return entity;    
    }

    public ToDo loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ToDo> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ToDo> list = new ArrayList<ToDo>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ToDo> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ToDo> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
