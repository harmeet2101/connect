package com.mboconnect.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.ViewSwitcher;

import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.swipe.SwipeLayout;
import com.mboconnect.R;
import com.mboconnect.activities.MessageListActivity;
import com.mboconnect.activities.OpportunityDetailsActivity;
import com.mboconnect.activities.OpportunityListActivity;
import com.mboconnect.activities.ProfileActivity;
import com.mboconnect.constants.EnumConstants;
import com.mboconnect.dialogs.OpportunityActionDialog;
import com.mboconnect.entities.Opportunity;
import com.mboconnect.entities.Profile;
import com.mboconnect.entities.SearchResults;
import com.mboconnect.listeners.APIResponseListner;
import com.mboconnect.model.DataModel;
import com.mboconnect.utils.Utils;
import com.mboconnect.views.OpportunityListView;
import com.tenpearls.android.components.EditText;
import com.tenpearls.android.components.TextView;
import com.tenpearls.android.network.VolleyManager;
import com.tenpearls.android.utilities.FontUtility;

import java.util.ArrayList;

/**
 * Created by tahir on 09/06/15.
 */
public class OpportunityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<Object> itemsList = new ArrayList<Object>();
    Context context;
    private EditText eTxtHeaderSearch;
    private TextSwitcher txtAllNumber;
    private TextSwitcher txtFavouriteNumber;
    boolean isTxtListAllCountRestarted = false;
    public static boolean isStatusNumberPressed;
    private int messageCount;

    public OpportunityListAdapter(Context context) {

        super();
        isStatusNumberPressed = false;
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == EnumConstants.ListViewType.Header.getValue()) {

            return new ViewHolderHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opportunity_list_header, parent, false));
        } else if (viewType == EnumConstants.ListViewType.ROW.getValue()) {

            return new ViewHolderItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_opportunity_list, parent, false), viewType);
        }

        return new ViewHolderSearchHeader(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false), viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int type = getItemViewType(position);

        if (type == EnumConstants.ListViewType.ROW.getValue()) {

            Opportunity opportunity = (Opportunity) itemsList.get(position);

            ((ViewHolderItem) holder).populateData(opportunity.getAddress().getCity() + ", " + opportunity.getAddress().getState(), opportunity.getTimestamp(), opportunity.getTitle(), opportunity.getCompanyName(), opportunity.getFormattedDateString(), opportunity.getBudget(), opportunity.getStatusToDisplay(), opportunity.isFavorite(), opportunity.isCompanyInfoVisible(), position);
        } else if (type == EnumConstants.ListViewType.Header.getValue()) {

            Profile data = (Profile) itemsList.get(0);
            if (data != null) {

                ((ViewHolderHeader) holder).populateHeader(data.getGreetings(), data.getDisplayName(), data.getDesignation(), data.getImageURL(), data.getProfileImage());
            }
        } else {
            SearchResults data = null;
            if (itemsList.get(0) instanceof SearchResults) {

                data = (SearchResults) itemsList.get(0);
            }
            String heading = context.getString(R.string.all_opportunities);
            if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.FAVOURITE) {

                heading = context.getString(R.string.favorite_opportunities);
            } else if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.SEARCH && data != null) {

                heading = data.getSearchResults() + " " + context.getString(R.string.opportunities);
            }
            ((ViewHolderSearchHeader) holder).populateData(heading);
        }
    }

    @Override
    public int getItemCount() {

        return itemsList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (itemsList.get(position) instanceof Opportunity) {

            return EnumConstants.ListViewType.ROW.getValue();
        } else if (itemsList.get(position) instanceof SearchResults) {

            return EnumConstants.ListViewType.SEARCH_HEADER.getValue();

        }

        return 0;
    }

    public TextSwitcher getTxtAllNumber() {

        return txtAllNumber;
    }

    public void setTxtAllNumber(TextSwitcher txtAllNumber) {

        this.txtAllNumber = txtAllNumber;
    }

    public TextSwitcher getTxtFavouriteNumber() {

        return txtFavouriteNumber;
    }

    public void setTxtFavouriteNumber(TextSwitcher txtFavouriteNumber) {

        this.txtFavouriteNumber = txtFavouriteNumber;
    }

    public EditText geteTxtHeaderSearch() {

        return eTxtHeaderSearch;
    }

    public void seteTxtHeaderSearch(EditText eTxtHeaderSearch) {

        this.eTxtHeaderSearch = eTxtHeaderSearch;
    }

    public int getMessageCount() {

        return messageCount;
    }

    public void setMessageCount(int messageCount) {

        this.messageCount = messageCount;
    }

    public void hideHeaderSearch() {

        Utils.hideSoftKeyboard((OpportunityListActivity) context);

        if (geteTxtHeaderSearch().getVisibility() == View.VISIBLE) {

            geteTxtHeaderSearch().setVisibility(View.GONE);
        }
    }

    // View Classes
    public class ViewHolderHeader extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout layoutSearch;

        private int activeNumber;

        private int allNumber;
        private NetworkImageView imgThumbnail;
        private TextView txtGreetings,
                txtName,
                txtDesignation;
        private APIResponseListner messageResponseListener;

        private MessageListAdapter messageAdapter;
        private TextSwitcher txtMessageCount;
        private RelativeLayout layoutTotal;
        private RelativeLayout layoutFavourite;
        private LinearLayout layoutStatus;
        private TextView txtOpportunities;
        private int currentMessageCount;

        public ViewHolderHeader(final View convertView) {

            super(convertView);
            initUI(convertView);
        }

        private void initUI(View convertView) {

            imgThumbnail = (NetworkImageView) convertView.findViewById(R.id.imgThumbnail);
            txtGreetings = (TextView) convertView.findViewById(R.id.txtGreetings);
            txtName = (TextView) convertView.findViewById(R.id.txtName);
            txtOpportunities = (TextView) convertView.findViewById(R.id.txtOpportunities);
            txtDesignation = (TextView) convertView.findViewById(R.id.txtDesignation);
            layoutStatus = (LinearLayout) convertView.findViewById(R.id.layoutStatus);

            convertView.setOnClickListener(this);
            setupMessageCountTextSwitcher(convertView);
            setupHeaderSearch(convertView);
            startStatusAnimationWithDelay(convertView);
            setupLayoutSearch(convertView);
            setupLayoutTotal(convertView);
            setupLayoutActive(convertView);
            setupLayoutMessages(convertView);
        }

        // Class Methods

        public void populateHeader(String greeting, String name, String designation, String imageUrl, Bitmap img) {

            txtGreetings.setText(greeting);
            txtName.setText(Utils.getName(name));
            txtDesignation.setText(designation);
            txtDesignation.setTextColor(Color.WHITE);

            if (img != null) {

                imgThumbnail.setImageBitmap(img);

            } else {

                imgThumbnail.setDefaultImageResId(R.drawable.thumbnail);
                imgThumbnail.setErrorImageResId(R.drawable.thumbnail);
                imgThumbnail.setImageUrl(imageUrl, VolleyManager.getInstance().getImageLoader());
            }
            imgThumbnail.setOnClickListener(imgThumbnailOnClickListener);
            startMessageCounter(messageCount);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    updateStatus();
                }
            }, 105);
        }

        private void updateStatus() {

            if (txtAllNumber != null) {

                isTxtListAllCountRestarted = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txtAllNumber.setText(DataModel.getOpportunityListTotalSize() + "");

                    }
                }, 70);

            }

            if (txtFavouriteNumber != null) {

                isTxtListAllCountRestarted = true;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        txtFavouriteNumber.setText(DataModel.getOpportunityListFavouriteSize() + "");

                    }
                }, 70);

            }
        }

        private void setupHeaderSearch(View convertView) {

            eTxtHeaderSearch = (EditText) convertView.findViewById(R.id.eTxtHeaderSearch);
            eTxtHeaderSearch.setOnEditorActionListener(eTxtHeaderSearchListener);
            eTxtHeaderSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        }

        private void startStatusAnimationWithDelay(final View convertView) {

            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {

                    setupAllTextSwitcher(convertView);
                    setupActiveTextSwitcher(convertView);

                }
            }, 100);
        }

        private void setupAllTextSwitcher(View convertView) {

            txtAllNumber = (TextSwitcher) convertView.findViewById(R.id.txtAllNumber);
            txtAllNumber.setInAnimation(context, R.anim.fade_in);
            txtAllNumber.setOutAnimation(context, R.anim.fade_out);
            txtAllNumber.setFactory(statusViewFactory);
            allNumber = 0;
            updateScore(txtAllNumber, EnumConstants.OpportunitiesType.ALL);
        }

        private void setupActiveTextSwitcher(View convertView) {

            txtFavouriteNumber = (TextSwitcher) convertView.findViewById(R.id.txtFavouriteNumber);
            txtFavouriteNumber.setInAnimation(context, R.anim.fade_in);
            txtFavouriteNumber.setOutAnimation(context, R.anim.fade_out);
            txtFavouriteNumber.setFactory(statusViewFactory);
            activeNumber = 0;
            updateScore(txtFavouriteNumber, EnumConstants.OpportunitiesType.FAVOURITE);
        }

        private void setupMessageCountTextSwitcher(View convertView) {

            txtMessageCount = (TextSwitcher) convertView.findViewById(R.id.txtMessageCount);
            txtMessageCount.setInAnimation(context, R.anim.fade_in);
            txtMessageCount.setOutAnimation(context, R.anim.fade_out);
            txtMessageCount.setFactory(messageCountViewFactory);

        }

        private void startMessageCounter(int messageCount) {

//            currentMessageCount = 0;
            updateMessageCount(messageCount);

        }

        private void updateScore(final TextSwitcher txtSwitcher, final EnumConstants.OpportunitiesType type) {

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {

                    int number;
                    if (type == EnumConstants.OpportunitiesType.ALL) {

                        number = (Math.abs(DataModel.getOpportunityListTotalSize() - allNumber) < 20) ? allNumber + 1 : allNumber + 20;
                        allNumber = number;
                        txtSwitcher.setText(String.valueOf(number));
                        if (number < DataModel.getOpportunityListTotalSize() && !isTxtListAllCountRestarted)
                            updateScore(txtSwitcher, type);

                    } else {

                        number = (Math.abs(DataModel.getOpportunityListFavouriteSize() - activeNumber) < 20) ? activeNumber + 1 : activeNumber + 20;
                        activeNumber = number;
                        txtSwitcher.setText(String.valueOf(number));
                        if (number < DataModel.getOpportunityListFavouriteSize())
                            updateScore(txtSwitcher, type);
                    }

                }
            }, 25);

        }

        private void updateMessageCount(final int finalCount) {

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {

                    int number;

                    number = (Math.abs(finalCount - currentMessageCount) < 5) ? currentMessageCount + 1 : currentMessageCount + 1;
                    if (number <= finalCount) {
                        currentMessageCount = number;
                        txtMessageCount.setText(String.valueOf(number));
                        txtMessageCount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        updateMessageCount(finalCount);

                    } else {


                        txtMessageCount.setText(String.valueOf(finalCount));
                        txtMessageCount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    }
                }
            }, 25);
        }

        ViewSwitcher.ViewFactory statusViewFactory = new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView t = new TextView(context);
                t.setTextColor(context.getResources().getColor(R.color.whiteBackground));
                t.setTextSize(30);
                t.setTypeface(FontUtility.getThinFontFromAssets(context));
                t.setGravity(Gravity.CENTER);
                return t;
            }
        };

        ViewSwitcher.ViewFactory messageCountViewFactory = new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView t = new TextView(context);
                t.setTextColor(context.getResources().getColor(R.color.whiteBackground));
                t.setTextSize(16);
                t.setTypeface(FontUtility.getRegularFontFromAssets(context));
                t.setText("0");
                return t;
            }
        };

        private void setupLayoutMessages(View convertView) {

            LinearLayout layoutMessages = (LinearLayout) convertView.findViewById(R.id.layoutMessages);
            layoutMessages.setOnClickListener(layoutMessagesOnClickListener);
        }

        private void setupLayoutSearch(View convertView) {

            layoutSearch = (RelativeLayout) convertView.findViewById(R.id.layoutSearch);
            layoutSearch.setOnClickListener(searchOnClickListener);

        }

        private void setupLayoutTotal(View convertView) {

            layoutTotal = (RelativeLayout) convertView.findViewById(R.id.layoutTotal);
            layoutTotal.setOnClickListener(totalOnClickListener);

        }

        private void setupLayoutActive(View convertView) {

            layoutFavourite = (RelativeLayout) convertView.findViewById(R.id.layoutFavourite);
            layoutFavourite.setOnClickListener(favouriteOnClickListener);

        }

        // Listeners

        View.OnClickListener imgThumbnailOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, ProfileActivity.class));
            }
        };

        @Override
        public void onClick(View v) {

            hideHeaderSearch();
        }

        private void hideStatusLayout() {

            layoutStatus.setVisibility(View.INVISIBLE);
            txtOpportunities.setVisibility(View.INVISIBLE);
        }

        View.OnClickListener totalOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataModel.setOpportunityListType(EnumConstants.OpportunitiesType.ALL);
                OpportunityListView.opportunitiesType = EnumConstants.OpportunitiesType.ALL;
                ((OpportunityListActivity) context).loadActiveList();
                isStatusNumberPressed = true;
                ((OpportunityListActivity) context).scrollToPosition(1);

            }
        };

        View.OnClickListener favouriteOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataModel.setOpportunityListType(EnumConstants.OpportunitiesType.FAVOURITE);
                OpportunityListView.opportunitiesType = EnumConstants.OpportunitiesType.FAVOURITE;
                ((OpportunityListActivity) context).loadActiveList();
                isStatusNumberPressed = true;
                ((OpportunityListActivity) context).scrollToPosition(1);

            }
        };

        View.OnClickListener searchOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eTxtHeaderSearch.setText("");
                eTxtHeaderSearch.setVisibility(View.VISIBLE);
                eTxtHeaderSearch.requestFocus();
                Utils.showSoftKeyboard((OpportunityListActivity) context);

            }
        };

        View.OnClickListener layoutMessagesOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, MessageListActivity.class);
                ((OpportunityListActivity) context).startActivityForResult(intent, OpportunityListActivity.RECORD_UPDATE_REQUEST);
            }
        };

        View.OnClickListener panelOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };

        View.OnTouchListener listViewMessagesOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        };

        TextView.OnEditorActionListener eTxtHeaderSearchListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(android.widget.TextView textView, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || event == null || event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    setToolBarSearchText(textView);
                    geteTxtHeaderSearch().setVisibility(View.GONE);

                }

                return false;
            }

        };

        private void setToolBarSearchText(android.widget.TextView v) {

            ((OpportunityListActivity) context).openSearch();
            ((OpportunityListActivity) context).setSettingMenuVisibility(false);
            ((OpportunityListActivity) context).getSearchView().setQuery(v.getText(), false);
        }

        // Header Methods

        public MessageListAdapter getMessageAdapter() {

            return messageAdapter;
        }

        public void setMessageAdapter(MessageListAdapter messageAdapter) {

            this.messageAdapter = messageAdapter;
        }

    }

    // Item Class
    public class ViewHolderItem extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        // each data item is just a string in this case
        TextView txtLocation,
                txtTimestamp,
                txtOpportunityTitle,
                txtCompanyName,
                txtDate,
                txtJobBudget,
                txtStatus, btnRespond;
        ImageView imgFavourite;
        com.mboconnect.components.ImageButton btnDelete;
        SwipeLayout swipe;

        public ViewHolderItem(View convertView, final int position) {

            super(convertView);

            swipe = (SwipeLayout) convertView.findViewById(R.id.swipe);
            txtLocation = (TextView) convertView.findViewById(R.id.txtLocation);
            txtTimestamp = (TextView) convertView.findViewById(R.id.txtTimestamp);
            txtOpportunityTitle = (TextView) convertView.findViewById(R.id.txtOpportunityTitle);
            txtCompanyName = (TextView) convertView.findViewById(R.id.txtCompanyName);
            txtDate = (TextView) convertView.findViewById(R.id.txtDate);
            txtJobBudget = (TextView) convertView.findViewById(R.id.txtJobBudget);
            txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);
            imgFavourite = (ImageView) convertView.findViewById(R.id.imgFavourite);
            btnRespond = (TextView) convertView.findViewById(R.id.btnRespond);
            btnDelete = (com.mboconnect.components.ImageButton) convertView.findViewById(R.id.btnDelete);

            swipe.setOnClickListener(ViewHolderItem.this);
            swipe.addSwipeListener(new SwipeLayout.SwipeListener() {
                @Override
                public void onClose(SwipeLayout layout) {

                    // when the SurfaceView totally cover the BottomView.
                }

                @Override
                public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

                    swipe.setOnClickListener(null);
                }

                @Override
                public void onStartOpen(SwipeLayout layout) {

                }

                @Override
                public void onOpen(SwipeLayout layout) {

                    // when the BottomView totally show.
                }

                @Override
                public void onStartClose(SwipeLayout layout) {

                }

                @Override
                public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            swipe.setOnClickListener(ViewHolderItem.this);
                        }
                    }, 10);
                }
            });

            if (itemsList.get(position) instanceof Opportunity && ((Opportunity) itemsList.get(position)).getStatusToDisplay().getValue() == EnumConstants.OpportunitiesStatus.NEW.getValue()) {

                swipe.setRightSwipeEnabled(true);
            } else {

                swipe.setRightSwipeEnabled(false);
            }

            btnRespond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemsList.get(position) instanceof Opportunity) {

                        onRespondPressed((Opportunity) itemsList.get(position));
                    }
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemsList.get(position) instanceof Opportunity) {

                        onDeletePressed((Opportunity) itemsList.get(position));
                    }
                }
            });
        }

        private void onDeletePressed(final Opportunity opportunity) {


            Utils.showAlert(null, context.getString(R.string.you_want_to_delete_opportunity), "OK", "CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ((OpportunityListActivity) context).deleteOpportunity(opportunity);
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            }, context);
        }

        private void onRespondPressed(final Opportunity opportunity) {

            Utils.showAlert(context.getString(R.string.are_you_sure), context.getString(R.string.by_responding_opportunity), context.getString(R.string.continue_caps),
                    context.getString(R.string.cancel_caps), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ((OpportunityListActivity) context).respondToOpportunity(opportunity);
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();
                        }
                    }, context);
        }

        public void populateData(String location, String timeStamp, String title, String companyName, String date, String budget, EnumConstants.OpportunitiesStatus value, boolean isFavorite, boolean isCompanyInfoVisible, final int position) {

            txtLocation.setText(location);
            txtTimestamp.setText(timeStamp);
            txtOpportunityTitle.setText(title);
            txtDate.setText(date);
            txtJobBudget.setText(budget);
            setStatus(value);
            txtCompanyName.setText(isCompanyInfoVisible ? companyName : context.getString(R.string.confidential));

            imgFavourite.setVisibility(isFavorite ? View.VISIBLE : View.GONE);

            if (value != EnumConstants.OpportunitiesStatus.RESPONDED && value != EnumConstants.OpportunitiesStatus.ACCEPTED) {

                swipe.setRightSwipeEnabled(true);
            } else {

                swipe.setRightSwipeEnabled(false);
            }

            if (itemsList.get(position) instanceof Opportunity)

                btnRespond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (itemsList.get(position) instanceof Opportunity) {

                            onRespondPressed((Opportunity) itemsList.get(position));
                        }
                    }
                });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (itemsList.get(position) instanceof Opportunity) {

                        onDeletePressed((Opportunity) itemsList.get(position));
                    }
                }
            });
        }

        private void setStatus(EnumConstants.OpportunitiesStatus value) {

            switch (value) {
                case ACCEPTED:
                    txtStatus.setText(context.getString(R.string.accepted));
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_green));
                    break;

                case RESPONDED:
                    txtStatus.setText(context.getString(R.string.responded_caps));
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_blue));
                    break;

                case NEW:
                    txtStatus.setText(context.getString(R.string.new_caps));
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.status_red));
                    break;

                default:
                    txtStatus.setText("");
                    txtStatus.setBackgroundColor(context.getResources().getColor(R.color.md__transparent));
                    break;
            }
        }

        @Override
        public void onClick(View v) {

            if (OpportunityListActivity.isLoading) {

                return;
            }
            Intent intent = new Intent(context, OpportunityDetailsActivity.class);

            int index = getLayoutPosition() - 2;
            if (DataModel.getOpportunityListType() == EnumConstants.OpportunitiesType.SEARCH) {
                index += 1;
            }
            intent.putExtra("selected_position", index);
            ((OpportunityListActivity) context).startActivityForResult(intent, OpportunityListActivity.RECORD_UPDATE_REQUEST);
        }

        @Override
        public boolean onLongClick(View v) {

            OpportunityActionDialog opportunityActionDialog = new OpportunityActionDialog(context, DataModel.getActiveOpportunities().get(getLayoutPosition() - 1), getLayoutPosition() - 1);
            opportunityActionDialog.show();
            return true;
        }
    }

    public class ViewHolderSearchHeader extends RecyclerView.ViewHolder {

        private final TextView txtResultReturns;

        public ViewHolderSearchHeader(View convertView, final int position) {

            super(convertView);
            txtResultReturns = (TextView) convertView.findViewById(R.id.txtResultReturns);

        }

        public void populateData(String data) {

            txtResultReturns.setText(data);
        }
    }
}
