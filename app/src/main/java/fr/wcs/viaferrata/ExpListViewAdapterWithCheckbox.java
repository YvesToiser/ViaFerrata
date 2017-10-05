package fr.wcs.viaferrata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint("UseSparseArrays")
public class ExpListViewAdapterWithCheckbox extends BaseExpandableListAdapter {

    // Define activity context
    private Context mContext;
    private HashMap<String, List<String>> mListDataChild;
    private ArrayList<String> mListDataGroup;
    private HashMap<Integer, boolean[]> mChildCheckStates;

    private static final String TAG = "MapActivity";
    private Map<Integer, Boolean> listeDiff = new HashMap<>();
    private Map<Integer, Boolean> listeZoneGeo = new HashMap<>();

    public ExpListViewAdapterWithCheckbox(Context context, ArrayList<String> listDataGroup, HashMap<String, List<String>> listDataChild){

        mContext = context;
        mListDataGroup = listDataGroup;
        mListDataChild = listDataChild;
        mChildCheckStates = new HashMap<>();
    }

    public Map<Integer, Boolean> getListeDiff() {
        return listeDiff;
    }

    public Map<Integer, Boolean> getListeZoneGeo() {
        return listeZoneGeo;
    }

    @Override
    public int getGroupCount() {
        return mListDataGroup.size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mListDataGroup.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String groupText = getGroup(groupPosition);
        GroupViewHolder groupViewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
            groupViewHolder = new GroupViewHolder();

            groupViewHolder.mGroupText = convertView.findViewById(R.id.lblListHeader);

            convertView.setTag(groupViewHolder);
        } else {

            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }

        groupViewHolder.mGroupText.setText(groupText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).size();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return mListDataChild.get(mListDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;
        String childText = getChild(mGroupPosition, mChildPosition);
        ChildViewHolder childViewHolder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);

            childViewHolder = new ChildViewHolder();

            childViewHolder.mChildText = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.lstcheckBox);

            convertView.setTag(R.layout.list_item, childViewHolder);

        } else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag(R.layout.list_item);
        }

        childViewHolder.mChildText.setText(childText);
        childViewHolder.mCheckBox.setOnCheckedChangeListener(null);

        if (mChildCheckStates.containsKey(mGroupPosition)) {
            boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
            childViewHolder.mCheckBox.setChecked(getChecked[mChildPosition]);

        }
        else {
            boolean getChecked[] = new boolean[getChildrenCount(mGroupPosition)];
            mChildCheckStates.put(mGroupPosition, getChecked);
            childViewHolder.mCheckBox.setChecked(false);
        }

        childViewHolder.mCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    Log.i(TAG, String.valueOf(mGroupPosition) + " - "+ String.valueOf(mChildPosition) + " is " + String.valueOf(isChecked));
                 //   Toast.makeText(mContext, String.valueOf(mGroupPosition) + " - "+ String.valueOf(mChildPosition) + " is " + String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    if (mGroupPosition == 0){
                        listeZoneGeo.put(mChildPosition, true);
                    }
                    if (mGroupPosition == 1){
                        listeDiff.put(mChildPosition, true);
                    }

                } else {

                    boolean getChecked[] = mChildCheckStates.get(mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put(mGroupPosition, getChecked);
                    Log.i(TAG, String.valueOf(mGroupPosition) + " - "+ String.valueOf(mChildPosition) + " is " + String.valueOf(isChecked));
                 //   Toast.makeText(mContext, String.valueOf(mGroupPosition) + " - "+ String.valueOf(mChildPosition) + " is " + String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                    if (mGroupPosition == 0){
                        listeZoneGeo.put(mChildPosition, false);
                    }
                    if (mGroupPosition == 1){
                        listeDiff.put(mChildPosition, false);
                    }
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void resetCheckboxes() {
        ChildViewHolder childViewHolder = new ChildViewHolder();
        childViewHolder.mCheckBox.setChecked(false);
    }

    public final class GroupViewHolder {

        TextView mGroupText;
    }

    public final class ChildViewHolder {

        TextView mChildText;
        CheckBox mCheckBox;
    }
}

