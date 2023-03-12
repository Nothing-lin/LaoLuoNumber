package com.nothinglin.nothingteam.fragment;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.nothinglin.nothingteam.R;
import com.nothinglin.nothingteam.adapter.FlowTagAdapter;
import com.nothinglin.nothingteam.base.BaseFragment;
import com.nothinglin.nothingteam.util.XToastUtils;
import com.xuexiang.xpage.annotation.Page;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.actionbar.TitleBar;
import com.xuexiang.xui.widget.flowlayout.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@Page(name = "首页")
public class MainFragment extends BaseFragment {
    @BindView(R.id.flowlayout_multi_select)
    FlowTagLayout mMultiFlowTagLayout;

    @BindView(R.id.inSelected)
    Button InSelected;

    @BindView(R.id.clear)
    Button btClear;

    @BindView(R.id.jump)
    Button btjump;

    @BindView(R.id.out)
    Button btOut;

    List<Integer> selectIntList = new ArrayList<>();
    List<Integer> containerList = new ArrayList<>();
    List<Integer> tempList;
    List<Integer> selecttempList = new ArrayList<>();
    Boolean isInSelect = true;

    String exportContent = new String();


    @Override
    protected TitleBar initTitle() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews() {

        FlowTagAdapter tagAdapter = new FlowTagAdapter(getContext());
        mMultiFlowTagLayout.setAdapter(tagAdapter);
        mMultiFlowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_MULTI);
        mMultiFlowTagLayout.setOnTagSelectListener(new FlowTagLayout.OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, int position, List<Integer> selectedList) {
//                XToastUtils.toast(getSelectedText(parent, selectedList));
                System.out.println(selectedList.toString());
                selectIntList = selectedList;
                exportContent = null;
                exportContent = getSelectedText(parent,selectedList);

            }
        });
        tagAdapter.addTags(ResUtils.getStringArray(R.array.tags_values));
//        tagAdapter.setSelectedPositions(2, 3, 4);


        for (int i = 0;i<49;i++){
            containerList.add(i);
        }

        tempList =new ArrayList<>(containerList);


        InSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exportContent = null;

                if (isInSelect) {
                    containerList = new ArrayList<>(tempList);
                    containerList.removeAll(selectIntList);

                    System.out.println(containerList.toString());

                    tagAdapter.clearSelection();
                    tagAdapter.setSelectedPositions(containerList);
                    isInSelect = false;
//                    XToastUtils.toast(containerList.toString());
//                    XToastUtils.toast(getSelectedText(mMultiFlowTagLayout,containerList));
                    exportContent = getSelectedText(mMultiFlowTagLayout,containerList);

                }else {
                    tagAdapter.clearSelection();

                    tagAdapter.setSelectedPositions(selectIntList);
//                    XToastUtils.toast(selectIntList.toString());
//                    XToastUtils.toast(getSelectedText(mMultiFlowTagLayout,selectIntList));
                    isInSelect = true;
                    exportContent = getSelectedText(mMultiFlowTagLayout,selectIntList);
                }

            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagAdapter.clearSelection();
                containerList = new ArrayList<>();
                selectIntList = new ArrayList<>();
            }
        });

        //复制且跳转到微信
        btjump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //复制
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText(null,exportContent);

                clipboardManager.setPrimaryClip(clipData);

                XToastUtils.toast("老罗，复制成功了！");

                //跳转微信
                try {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setComponent(cmp);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // TODO: handle exception
                    XToastUtils.toast("检查到您手机没有安装微信，请安装后使用该功能");
                }
            }
        });


        btOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }


    private String getSelectedText(FlowTagLayout parent, List<Integer> selectedList) {
        StringBuilder sb = new StringBuilder("输出：\n");
        for (int index : selectedList) {
            sb.append(parent.getAdapter().getItem(index));
            sb.append(", ");
        }
        return sb.toString();
    }

}
