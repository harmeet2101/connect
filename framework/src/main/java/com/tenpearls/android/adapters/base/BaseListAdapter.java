/*
 * 10Pearls - Android Framework v1.0
 * 
 * The contributors of the framework are responsible for releasing 
 * new patches and make modifications to the code base. Any bug or
 * suggestion encountered while using the framework should be
 * communicated to any of the contributors.
 * 
 * Contributors:
 * 
 * Ali Mehmood       - ali.mehmood@tenpearls.com
 * Arsalan Ahmed     - arsalan.ahmed@tenpearls.com
 * M. Azfar Siddiqui - azfar.siddiqui@tenpearls.com
 * Syed Khalilullah  - syed.khalilullah@tenpearls.com
 */
package com.tenpearls.android.adapters.base;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * An extended {@link BaseAdapter} class, most likely to be used in conjunction
 * with a {@link ListView}. Provides built in support for showing a 'Load more'
 * and 'Add new item' view rows. Also implements {@link OnItemClickListener}
 * internally. You may need to override the following methods based on your
 * configuration:
 * 
 * <ul>
 * <li>{@code onListViewAddNewItemAction}</li>
 * <li>{@code onListViewLoadMoreAction}</li>
 * <li>{@code onListViewItemClickedAction}</li>
 * </ul>
 * 
 * @author 10Pearls
 * 
 * @param <T> The class type of the Adapter's data source.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter implements OnItemClickListener {

	private List<T>  mItems;
	private ListView mListView;

	/**
	 * Return the list of objects to be used as data source.
	 * 
	 * @return The list of items.
	 */
	protected abstract List<T> getItems ();

	/**
	 * Return the visibility of the 'Load more' view.
	 * 
	 * @return {@code true} if you need to show 'Load more', {@code else}
	 *         otherwise.
	 */
	protected abstract boolean shouldShowLoadMoreRow ();

	/**
	 * Return the visibility of the 'Add new item' view.
	 * 
	 * @return {@code true} if you need to show 'Add new item', {@code else}
	 *         otherwise.
	 */
	protected abstract boolean shouldShowAddNewItemRow ();

	/**
	 * Return the view to be displayed as 'Load more' row.
	 * 
	 * @param convertView The cached view.
	 * @return The 'Load more' view.
	 */
	protected abstract View getLoadMoreView (View convertView);

	/**
	 * Return the view to be displayed as 'Add new item' row.
	 * 
	 * @param convertView The cached view.
	 * @return The 'Add new item' view.
	 */
	protected abstract View getAddNewItemView (View convertView);

	/**
	 * Return the view to be shown for item row.
	 * 
	 * @param position The position index in ListView.
	 * @param item The object corresponding to the current position.
	 * @param convertView The cached view.
	 * 
	 * @return The view to be shown for an item row.
	 */
	protected abstract View getItemView (int position, T item, View convertView);

	/**
	 * Overloaded constructor.
	 * 
	 * @param listView The list view object that will use this adapter.
	 */
	public BaseListAdapter (ListView listView) {

		mItems = getItems ();
		mListView = listView;
		mListView.setOnItemClickListener (this);
	}

	/**
	 * Returns the number of rows to render, keeping the additional views into
	 * account.
	 * 
	 * @return The total number of rows to displays.
	 */
	@Override
	public int getCount () {

		return getItemsCount () + getAdditionalRowsCount ();
	}

	/**
	 * Returns the object associated with the current position on the adapter.
	 * 
	 * @return T The object for current position.
	 */
	@Override
	public T getItem (int position) {

		return mItems.get (position);
	}

	/**
	 * Returns the Id associated with the current position.
	 * 
	 * @return The row Id.
	 */
	@Override
	public long getItemId (int position) {

		return position;
	}

	/**
	 * Returns the number of view types to be presented in adapter.
	 * 
	 * @return View types count.
	 */
	@Override
	public int getViewTypeCount () {

		return 1 + getAdditionalRowsCount ();
	}

	/**
	 * Gets the type of view associated with the current position.
	 * 
	 * @return View type id.
	 */
	@Override
	public int getItemViewType (int position) {

		int viewType = 0;

		if (isItemRow (position)) {
			viewType = 0;
		}
		else if (shouldShowAddNewItemRow () && isAddNewItemRow (position)) {
			viewType = 1;
		}
		else if (shouldShowLoadMoreRow () && shouldShowAddNewItemRow () && isLoadMoreRow (position)) {
			viewType = 2;
		}
		else if (shouldShowLoadMoreRow () && isLoadMoreRow (position)) {
			viewType = 1;
		}

		return viewType;
	}

	/**
	 * Returns the view to be displayed against the current position.
	 * 
	 * @return The view for current position.
	 */
	@Override
	public View getView (int position, View convertView, ViewGroup arg2) {

		if (isItemRow (position)) {
			T item = (shouldShowAddNewItemRow ()) ? getItem (position - 1) : getItem (position);
			convertView = getItemView (position, item, convertView);
		}
		else if (isAddNewItemRow (position)) {
			convertView = getAddNewItemView (convertView);
		}
		else if (isLoadMoreRow (position)) {
			convertView = getLoadMoreView (convertView);
		}

		return convertView;
	}

	private int getAdditionalRowsCount () {

		int additionalRowsCount = 0;

		if (shouldShowAddNewItemRow ())
			additionalRowsCount++;
		if (shouldShowLoadMoreRow ())
			additionalRowsCount++;

		return additionalRowsCount;
	}

	private boolean isItemRow (int position) {

		if (shouldShowAddNewItemRow ()) {
			if (position > 0 && position <= getItemsCount ()) {
				return true;
			}
		}
		else {
			if (position >= 0 && position < getItemsCount ()) {
				return true;
			}
		}

		return false;
	}

	private boolean isLoadMoreRow (int position) {

		if (!shouldShowLoadMoreRow ())
			return false;

		if (shouldShowAddNewItemRow () && position == getItemsCount () + 1) {
			return true;
		}
		else if (position == getItemsCount ()) {
			return true;
		}

		return false;
	}

	private boolean isAddNewItemRow (int position) {

		if (!shouldShowAddNewItemRow ())
			return false;

		return (position == 0) ? true : false;
	}

	private int getItemsCount () {

		return (mItems != null) ? mItems.size () : 0;
	}

	/**
	 * Handles {@code onItemClick} of the {@link ListView}. Internally fires
	 * callback methods of {@link BaseListAdapterListener}.
	 */
	@Override
	public void onItemClick (AdapterView<?> arg0, View arg1, int position, long arg3) {

		if (isAddNewItemRow (position)) {
			onListViewAddNewItemAction ();
		}
		else if (isItemRow (position)) {
			if (shouldShowAddNewItemRow ()) {
				position--;
			}
			onListViewItemClickedAction (getItem (position), position);
		}
		else if (isLoadMoreRow (position)) {
			onListViewLoadMoreAction ();
		}
	}

	/**
	 * Called when 'Load more' view is clicked. You need to override this when
	 * {@code shouldShowLoadMore} returns {@code true}, do not call super's
	 * implementation.
	 */
	protected void onListViewLoadMoreAction () {

		if (shouldShowLoadMoreRow ()) {
			throw new IllegalStateException (this.getClass ().getName () + " >> You must override onListViewLoadMoreAction() method.");
		}
	}

	/**
	 * Called when 'Add new item' view is clicked. You need to override this
	 * when {@code shouldShowAddNewItem} returns {@code true}, do not call
	 * super's implementation.
	 */
	protected void onListViewAddNewItemAction () {

		if (shouldShowAddNewItemRow ()) {
			throw new IllegalStateException (this.getClass ().getName () + " >> You must override onListViewAddNewItemAction() method.");
		}
	}

	/**
	 * Called when an item row is clicked. You must override this, do not call
	 * super's implementation.
	 * 
	 * @param item The object associated with the clicked view.
	 * @param position The position of the adapter clicked.
	 */
	protected void onListViewItemClickedAction (T item, int position) {

		throw new IllegalStateException (this.getClass ().getName () + " >> You must override onListViewItemClickedAction() method.");
	}

}
