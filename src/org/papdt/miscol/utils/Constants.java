package org.papdt.miscol.utils;

public class Constants {

	public static final class Preferences {

		public static final class FileNames {
			public static final String GENERAL = "general";
		}

		public static final class Keys {
			public static final String HAS_EVER_STARTED = "hasEverStarted";
		}
	}

	/** 数据库管理用各常量,子类为各表项 */
	public static final class Databases {

		public interface IDbWithIdAndName {
			/** 编号(数据库自动管理) */
			public static final String KEY_INT_ID = "_id";
			/** 科目名称 */
			public static final String KEY_STRING_NAME = "name";
			// item_count 被取消
		}

		/** 数据库版本 */
		public static final int VERSION = 1;
		/** 数据库文件名 */
		public static final String FILE_NAME = "mistakes.db";

		/** 保存错题详细信息的主表 */
		public static final class Mistakes {
			/** 数据表名 */
			public static final String TABLE_NAME = "mistakes";
			/** 编号(数据库自动管理) */
			public static final String KEY_INT_ID = "_id";
			/** 添加时间 */
			public static final String KEY_DATETIME_ADD_TIME = "add_time";
			/** 最后修改时间 */
			public static final String KEY_DATETIME_LAST_MODIFY_TIME = "last_modify_time";
			/** 标题 */
			public static final String KEY_STRING_TITLE = "title";
			/** 题目类型编号 */
			public static final String KEY_INT_TYPE_ID = "type_id";
			/** 问题文本 */
			public static final String KEY_STRING_QUESTION_TEXT = "question_text";
			/** 问题图片 */
			public static final String KEY_INT_QUESTION_PHOTO_ID = "question_photo_id";
			/** 答案文本 */
			public static final String KEY_STRING_ANSWER_TEXT = "answer_text";
			/** 答案图片 */
			public static final String KEY_INT_ANSWER_PHOTO_ID = "answer_photo_id";
			/** 上次复习时间(如果有) */
			public static final String KEY_DATETIME_LAST_REVIEW_TIME = "last_review_time";
			/** 复习次数(如果有) */
			public static final String KEY_INT_REVIEW_TIMES = "review_times";
			/** 复习正确次数(如果有) */
			public static final String KEY_INT_REVIEW_CORRECT_TIMES = "review_correct_times";
			/** 正确率(如果有) */
			public static final String KEY_REAL_CORRECT_RATE = "correct_rate";
			/** 科目 */
			public static final String KEY_INT_SUBJECT_ID = "subject_id";
			/** 年级 */
			public static final String KEY_INT_GRADE_ID = "grade_id";
			/** 标签(逗号分隔) */
			public static final String KEY_STRING_TAG_IDS = "tag_ids";
			/** 是否重点(以0和1区分) */
			public static final String KEY_INT_IS_ISTARRED = "is_starred";
		}

		/** 科目信息 */
		public static final class Subjects implements IDbWithIdAndName {
			/** 数据表名 */
			public static final String TABLE_NAME = "subjects";
		}

		/** 年级信息 */
		public static final class Grades implements IDbWithIdAndName {
			/** 数据表名 */
			public static final String TABLE_NAME = "grades";
		}

		/** 标签信息 */
		public static final class Tags implements IDbWithIdAndName {
			/** 数据表名 */
			public static final String TABLE_NAME = "tags";
		}

		/** 题目类型(选择/判断/填空等) */
		public static final class QuestionType implements IDbWithIdAndName {
			/** 数据表名 */
			public static final String TABLE_NAME = "question_type";
		}

		/** 引用的各文件信息 */
		public static final class Files {
			/** 数据表名 */
			public static final String TABLE_NAME = "files";
			/** 编号(数据库自动管理) */
			public static final String KEY_INT_ID = "_id";
			/** 文件类型(小写) */
			public static final String KEY_STRING_TYPE = "type";
			/** 文件绝对路径 */
			public static final String KEY_STRING_PATH = "path";
		}

		/** 用于操作数据库表结构的语句 */
		public static final class SqlStatements {

			/** 创建表前缀 */
			public static final String CREATE = "CREATE TABLE IF NOT EXISTS ";
			/** 删除表前缀 */
			public static final String DROP = "DROP TABLE IF EXISTS ";

			public static final String SELECT_ALL_ITEM = Mistakes.KEY_INT_ID
					+ "," + Mistakes.KEY_DATETIME_ADD_TIME + ","
					+ Mistakes.KEY_DATETIME_LAST_MODIFY_TIME + ","
					+ Mistakes.KEY_STRING_TITLE + ","
					+ Mistakes.KEY_INT_TYPE_ID + ","
					+ Mistakes.KEY_STRING_QUESTION_TEXT + ","
					+ Mistakes.KEY_INT_QUESTION_PHOTO_ID + ","
					+ Mistakes.KEY_STRING_ANSWER_TEXT + ","
					+ Mistakes.KEY_INT_ANSWER_PHOTO_ID + ","
					+ Mistakes.KEY_DATETIME_LAST_REVIEW_TIME + ","
					+ Mistakes.KEY_INT_REVIEW_TIMES + ","
					+ Mistakes.KEY_INT_REVIEW_CORRECT_TIMES + ","
					+ Mistakes.KEY_REAL_CORRECT_RATE + ","
					+ Mistakes.KEY_INT_SUBJECT_ID + ","
					+ Mistakes.KEY_INT_GRADE_ID + ","
					+ Mistakes.KEY_STRING_TAG_IDS + ","
					+ Mistakes.KEY_INT_IS_ISTARRED;

			public static final String MISTAKES = Mistakes.TABLE_NAME + "("
					+ Mistakes.KEY_INT_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
					+ Mistakes.KEY_DATETIME_ADD_TIME + " DATETIME NOT NULL"
					+ ", " + Mistakes.KEY_DATETIME_LAST_MODIFY_TIME
					+ " DATETIME" + ", " + Mistakes.KEY_STRING_TITLE
					+ " TEXT NOT NULL" + ", " + Mistakes.KEY_INT_TYPE_ID
					+ " INTEGER NOT NULL" + ", "
					+ Mistakes.KEY_STRING_QUESTION_TEXT + " TEXT NOT NULL"
					+ ", " + Mistakes.KEY_INT_QUESTION_PHOTO_ID + " INTEGER"
					+ ", " + Mistakes.KEY_STRING_ANSWER_TEXT + " TEXT NOT NULL"
					+ ", " + Mistakes.KEY_INT_ANSWER_PHOTO_ID + " INTEGER"
					+ ", " + Mistakes.KEY_DATETIME_LAST_REVIEW_TIME
					+ " DATETIME" + ", " + Mistakes.KEY_INT_REVIEW_TIMES
					+ " INTEGER" + ", " + Mistakes.KEY_INT_REVIEW_CORRECT_TIMES
					+ " INTEGER" + ", " + Mistakes.KEY_REAL_CORRECT_RATE
					+ " REAL" + ", " + Mistakes.KEY_INT_SUBJECT_ID
					+ " INTEGER NOT NULL" + ", " + Mistakes.KEY_INT_GRADE_ID
					+ " INTEGER NOT NULL" + ", " + Mistakes.KEY_STRING_TAG_IDS
					+ " TEXT" + ", " + Mistakes.KEY_INT_IS_ISTARRED
					+ " INTEGER DEFAULT 0" + ")";

			public static final String SUBJECTS = Subjects.TABLE_NAME + "("
					+ Subjects.KEY_INT_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
					+ Subjects.KEY_STRING_NAME + " TEXT NOT NULL" + ")";

			public static final String GRADES = Grades.TABLE_NAME + "("
					+ Grades.KEY_INT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
					+ ", " + Grades.KEY_STRING_NAME + " TEXT NOT NULL" + ")";

			public static final String TAGS = Tags.TABLE_NAME + "("
					+ Tags.KEY_INT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
					+ ", " + Tags.KEY_STRING_NAME + " TEXT NOT NULL" + ")";

			public static final String QUESTION_TYPE = QuestionType.TABLE_NAME
					+ "(" + QuestionType.KEY_INT_ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT" + ", "
					+ QuestionType.KEY_STRING_NAME + " TEXT NOT NULL" + ")";

			public static final String FILES = Files.TABLE_NAME + "("
					+ Files.KEY_INT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT"
					+ ", " + Files.KEY_STRING_TYPE + " TEXT NOT NULL" + ", "
					+ Files.KEY_STRING_PATH + " TEXT NOT NULL" + ")";

		}
	}

}
