package com.himmiractivity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.himmiractivity.Utils.ToastUtil;
import com.himmiractivity.base.BaseBusActivity;
import com.himmiractivity.entity.DafalutUserRoom;
import com.himmiractivity.entity.DataServerBean;
import com.himmiractivity.entity.DeviceInfoBean;
import com.himmiractivity.entity.ImageBean;
import com.himmiractivity.interfaces.StatisConstans;
import com.himmiractivity.request.DataServerConfigRequest;
import com.himmiractivity.request.ReceiveUserDeviceInfoRequest;
import com.himmiractivity.view.ChangeAddressDialog;
import com.himmiractivity.view.ClearEditText;

import java.io.Serializable;
import java.util.List;

import activity.hamir.com.himmir.R;
import butterknife.BindView;

public class InformationComitActivity extends BaseBusActivity {
    @BindView(R.id.et_username)
    ClearEditText etUsername;
    @BindView(R.id.et_full_address)
    ClearEditText etFullAddress;
    @BindView(R.id.tv_city_choise)
    Button tvCityChoise;
    @BindView(R.id.tv_install_site)
    Button tvInstallSite;
    @BindView(R.id.tv_unit_type)
    TextView tvUnitType;
    @BindView(R.id.tv_manufacturing_date)
    TextView tvManufacturingDate;
    @BindView(R.id.bt_stroe)
    ClearEditText btStroe;
    @BindView(R.id.btn_choose_room)
    Button btnChooseRoom;
    @BindView(R.id.btn_qr_next)
    Button btnQrNext;
    DataServerBean dataServerBean;
    DeviceInfoBean deviceInfoBean;
    String provinceSite, citySite, areaSite;
    String provinceChoise, cityChoise, areaChoise;
    ChangeAddressDialog mChangeAddressDialog;
    ChangeAddressDialog dialog;
    public final int ACTIVITY_MORE_MESSAGE = 169;
    List<DafalutUserRoom> list;
    int position = -1;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case StatisConstans.CONFIG_REGULAR:
                    dataServerBean = (DataServerBean) msg.obj;
                    break;
                case StatisConstans.MSG_RECEIVED_REGULAR:
                    ImageBean imageBean = (ImageBean) msg.obj;
                    ToastUtil.show(InformationComitActivity.this, imageBean.getSuccess());
                    Intent intent = new Intent(InformationComitActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
            }
            return false;
        }
    });

    @Override
    protected int getContentLayoutId() {
        return R.layout.information_comit;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setMidTxt("信息确认");
        initTitleBar();
        tvCityChoise.setOnClickListener(this);
        tvInstallSite.setOnClickListener(this);
        btnChooseRoom.setOnClickListener(this);
        btnQrNext.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            deviceInfoBean = (DeviceInfoBean) getIntent().getExtras().getSerializable("device_info");
            tvUnitType.setText(deviceInfoBean.getDeviceInfo().getDevice_type());
            tvManufacturingDate.setText(deviceInfoBean.getDeviceInfo().getDevice_shipmenttime());
        }
        try {
            DataServerConfigRequest dataServerConfigRequest = new DataServerConfigRequest(sharedPreferencesDB, handler, InformationComitActivity.this);
            dataServerConfigRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initData() {
        provinceSite = sharedPreferencesDB.getString("province", "");
        citySite = sharedPreferencesDB.getString("city", "");
        areaSite = sharedPreferencesDB.getString("area", "");
        provinceChoise = sharedPreferencesDB.getString("province", "");
        cityChoise = sharedPreferencesDB.getString("city", "");
        areaChoise = sharedPreferencesDB.getString("area", "");
        tvCityChoise.setText(provinceChoise + "省-" + cityChoise + "市-" + areaChoise);
        tvInstallSite.setText(provinceSite + "省-" + citySite + "市-" + areaSite);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_MORE_MESSAGE && resultCode == Activity.RESULT_OK) {
            if (data.getExtras().get(StatisConstans.KEY_SAVE_LABLE) != null) {
                Bundle bundle = data.getExtras();
                if (list != null && list.size() > 0) {
                    list.clear();
                }
                position = bundle.getInt("position");
                list = (List<DafalutUserRoom>) bundle.get(StatisConstans.KEY_SAVE_LABLE);
                btnChooseRoom.setText(list.get(position).getRoom_name());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_left:
                finish();
//                startActivity(new Intent(InformationComitActivity.this, EditTimeActivity.class));
                break;
            case R.id.btn_qr_next:
                confirmation();
                break;
            case R.id.btn_choose_room:
                Intent intent = new Intent(InformationComitActivity.this,
                        InstallRoomActivity.class);
                Bundle extras = new Bundle();
                if (list != null && list.size() > 0) {
                    extras.putSerializable("list", (Serializable) list);
                }
                intent.putExtras(extras);
                InformationComitActivity.this.startActivityForResult(intent,
                        ACTIVITY_MORE_MESSAGE);
                break;
            case R.id.tv_city_choise:
                if (mChangeAddressDialog == null) {
                    mChangeAddressDialog = new ChangeAddressDialog(
                            InformationComitActivity.this);
                }
                mChangeAddressDialog.setAddress(provinceChoise, cityChoise, areaChoise);
                mChangeAddressDialog.show();
                mChangeAddressDialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                provinceChoise = province;
                                cityChoise = city;
                                areaChoise = area;
                                tvCityChoise.setText(provinceChoise + "省-" + cityChoise + "市-" + areaChoise);
                            }
                        });
                break;
            case R.id.tv_install_site:
                if (dialog == null) {
                    dialog = new ChangeAddressDialog(
                            InformationComitActivity.this);
                }
                dialog.setAddress(provinceSite, citySite, areaSite);
                dialog.show();
                dialog
                        .setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                provinceSite = province;
                                citySite = city;
                                areaSite = area;
                                tvInstallSite.setText(provinceSite + "省-" + citySite + "市-" + areaSite);
                            }
                        });
                break;
            default:
                break;
        }
    }

    //验证信息是否通过
    private void confirmation() {
        String name = etUsername.getText().toString().trim();
        String address = etFullAddress.getText().toString().trim();
        String stroe = btStroe.getText().toString().trim();
        String room = btnChooseRoom.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(this, "用户姓名不能为空!");
            return;
        } else if (TextUtils.isEmpty(stroe)) {
            ToastUtil.show(this, "门店名称不能为空!");
            return;
        } else if (TextUtils.isEmpty(address)) {
            ToastUtil.show(this, "详细地址不能为空");
            return;
        } else if (TextUtils.isEmpty(room) || room.contains("选择安装房间")) {
            ToastUtil.show(this, "请选择安装房间");
            return;
        }
        ReceiveUserDeviceInfoRequest receiveUserDeviceInfoRequest = new ReceiveUserDeviceInfoRequest(sharedPreferencesDB, InformationComitActivity.this, handler, room, name, deviceInfoBean.getDeviceInfo().getDevice_mac(), deviceInfoBean.getDeviceInfo().getDevice_sn());
        try {
            receiveUserDeviceInfoRequest.setBuyAddress(provinceChoise + "省", cityChoise + "市", areaChoise, stroe);
            receiveUserDeviceInfoRequest.setinstallAddress(provinceSite + "省", citySite + "市", areaSite, address);
            receiveUserDeviceInfoRequest.requestCode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
