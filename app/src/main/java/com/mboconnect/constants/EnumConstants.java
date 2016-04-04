package com.mboconnect.constants;

/**
 * Created by tahir on 11/06/15.
 */
public class EnumConstants {

	public enum ListViewType {

		Header (0), ROW (1), SEARCH_HEADER (2);

		private final int value;

		ListViewType (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

	public enum MessageType {

		MANAGER (0), ASSOCIATE (1), SECTION_HEADER (2);

		private final int value;

		MessageType (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

	public enum OpportunitiesType {

		ALL (0), FAVOURITE (1), SEARCH (2);

		private final int value;

		OpportunitiesType (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

	public enum MemberType {

		AUTHOR (0), MANAGER (1), ASSOCIATE (2);

		private final int value;

		MemberType (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

	public enum OpportunitiesStatus {

		ACCEPTED (0), RESPONDED (1), NEW (2), KEY_CLOSED (3);

		private final int value;

		OpportunitiesStatus (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

	public enum ConversationTypes {

		CLOSED (0), ACTIVE (1);

		private final int value;

		ConversationTypes (int i) {

			this.value = i;
		}

		public int getValue () {

			return value;
		}
	}

}
