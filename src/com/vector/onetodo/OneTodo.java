package com.darussalam.emjoyyourlife;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class OneTodo {

	private static final String SCHEMA_PACKAGE = "com.vector.onetodo.db.gen";
	private static final String SCHEMA_OUTPUT_DIR = "../DaoExample/src";

	public static void main(String[] args) throws Exception {
		Schema dbSchema = new Schema(1, SCHEMA_PACKAGE);
		generateSchema(dbSchema);
		new DaoGenerator().generateAll(dbSchema, SCHEMA_OUTPUT_DIR);
	}

	public static void generateSchema(Schema schema) {

		Entity checklist = schema.addEntity("CheckList");
		checklist.addIdProperty().primaryKey().autoincrement();
		checklist.addStringProperty("title");

		/*Entity assign = schema.addEntity("Assign");
		assign.addIdProperty().primaryKey().autoincrement().getProperty();
		assign.addStringProperty("name");

		Entity share = schema.addEntity("Share");
		share.addIdProperty().primaryKey().autoincrement().getProperty();
		share.addStringProperty("name");

		Entity friends = schema.addEntity("Friends");
		friends.addIdProperty().primaryKey().autoincrement().getProperty();
		friends.addStringProperty("fname");
		friends.addStringProperty("lname");
		friends.addStringProperty("email");
		friends.addStringProperty("uu_id");
		Property friendsID = assign.addLongProperty("friends_id").notNull()
				.getProperty();
		friends.addToMany(share, friendsID);
		friends.addToMany(assign, friendsID);*/

		Entity remainder = schema.addEntity("Reminder");
		remainder.addIdProperty().primaryKey().autoincrement().getProperty();
		remainder.addBooleanProperty("is_time_location");
		remainder.addBooleanProperty("is_alertNotification");
		remainder.addBooleanProperty("is_alertEmail");
		remainder.addStringProperty("location");
		remainder.addStringProperty("location_tag");
		remainder.addIntProperty("location_type");
		remainder.addLongProperty("time");
		
		
		Entity repeat = schema.addEntity("Repeat");
		repeat.addIdProperty().primaryKey().autoincrement().getProperty();
		repeat.addStringProperty("repeat_interval");
		repeat.addStringProperty("repeat_until");
		repeat.addBooleanProperty("is_forever");
		
		
		Entity label = schema.addEntity("Label");
		label.addIdProperty().primaryKey().autoincrement().getProperty();
		label.addStringProperty("label_name");

		Entity attach = schema.addEntity("Attach");
		attach.addIdProperty().primaryKey().autoincrement().getProperty();
		attach.addStringProperty("attach_path");
		attach.addStringProperty("attach_type");
		
		Entity comment = schema.addEntity("Comment");
		comment.addIdProperty().primaryKey().autoincrement().getProperty();
		comment.addStringProperty("comment");
		comment.addStringProperty("comment_type");
		comment.addStringProperty("comment_date");
		
		Entity todo = schema.addEntity("ToDo");
		todo.addIdProperty().primaryKey().autoincrement();
		todo.addIntProperty("user_id");
		todo.addIntProperty("todo_type_id");
		todo.addIntProperty("parent");
		todo.addStringProperty("title");
		todo.addBooleanProperty("is_allday");
		todo.addStringProperty("start_date");
		todo.addStringProperty("end_date");
		todo.addStringProperty("location");
		todo.addIntProperty("priority");
		todo.addStringProperty("notes");
		
		/*todo.addLongProperty("time");
		todo.addLongProperty("repeat");
		todo.addBooleanProperty("is_delayed");
		todo.addBooleanProperty("is_priority");
		todo.addBooleanProperty("is_align");
		todo.addBooleanProperty("is_shared");
		todo.addStringProperty("location");*/
		Property todoID = todo.addLongProperty("todo_id").notNull()
				.getProperty();
		todo.addToOne(checklist, todoID);
	/*	todo.addToOne(assign, todoID);
		todo.addToOne(share, todoID);*/
		todo.addToOne(label, todoID);
		
		Property remainderID = remainder.addLongProperty("reminder_id").notNull()
				.getProperty();
		remainder.addToOne(todo, remainderID);
	}
}