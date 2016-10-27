package net.devcats.squirrel;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import net.devcats.squirrel.Adapters.SquirrelFragmentAdapter;
import net.devcats.squirrel.Fragments.LoginFragment;
import net.devcats.squirrel.Fragments.ModelsFragment;
import net.devcats.squirrel.Fragments.MyAccountFragment;
import net.devcats.squirrel.Fragments.ModelFragment;
import net.devcats.squirrel.Fragments.MyAlbumsFragment;
import net.devcats.squirrel.Fragments.RegisterFragment;
import net.devcats.squirrel.Fragments.ViewModelImagesFragment;
import net.devcats.squirrel.Handlers.ModelDataHandler;
import net.devcats.squirrel.Handlers.UserHandler;
import net.devcats.squirrel.Interfaces.OnModelSelectedListener;
import net.devcats.squirrel.Models.ModelData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements OnModelSelectedListener, ModelsFragment.FilterChangeCallbacks, ModelFragment.ModelDetailsCallbacks, RegisterFragment.RegisterFragmentCallbacks {

    private Context mContext;
    private ActionBar actionBar;
    private DrawerLayout drawerLayout;
    private ViewPager vpFragmentPager;
    private SquirrelFragmentAdapter myFragmentAdapter;
    private LoginFragment loginFragment;
    private MyAccountFragment accountFragment;
    private RegisterFragment registerFragment;
    private MyAlbumsFragment profileFragment;
    private ModelsFragment allModelsFragment;
    private ModelFragment modelFragment;
    private ViewModelImagesFragment viewModelImagesFragment;

    private Menu mMenu;

    private RadioButton rbAllModels;
    private RadioButton rbMyModels;

    private UserHandler userHandler;
    private ModelDataHandler modelDataHandler;

    private EditText etModelSearch;

    private CheckBox chkFemale;
    private CheckBox chkMale;
    private CheckBox chkCouple;
    private CheckBox chkGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        actionBar = getActionBar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Initialize Handlers
        userHandler = UserHandler.getInstance(mContext);
        modelDataHandler = ModelDataHandler.getInstance(mContext);

        if (savedInstanceState != null) {
            return;
        }

        initializeMenuDrawer();
        initializeFragments();

    }

    private void initializeFragments() {
        List<Fragment> fragments = new ArrayList<>(3);

        // Display the correct user screen. Login or My Account
        if (userHandler.getUserID() == 0) {
            fragments.add(loginFragment = LoginFragment.newInstance());
        } else {
            fragments.add(accountFragment = MyAccountFragment.newInstance());
        }

        fragments.add(allModelsFragment = ModelsFragment.newInstance());
        fragments.add(profileFragment = MyAlbumsFragment.newInstance());

        myFragmentAdapter = new SquirrelFragmentAdapter(mContext, getSupportFragmentManager(), fragments);

        vpFragmentPager = (ViewPager) findViewById(R.id.vpFragmentPager);
        vpFragmentPager.setAdapter(myFragmentAdapter);
        vpFragmentPager.setCurrentItem(1);
        vpFragmentPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mMenu.findItem(R.id.action_filter).setVisible(true);
                    mMenu.findItem(R.id.action_add).setVisible(false);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else if (position == 2) {
                    mMenu.findItem(R.id.action_add).setVisible(true);
                    mMenu.findItem(R.id.action_filter).setVisible(false);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else {
                    mMenu.findItem(R.id.action_filter).setVisible(false);
                    mMenu.findItem(R.id.action_add).setVisible(false);
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @SuppressWarnings("deprecation")
    private void initializeMenuDrawer() {

        // Get pointers
        etModelSearch = (EditText) findViewById(R.id.etModelSearch);
        rbAllModels = (RadioButton) findViewById(R.id.rbAllModels);
        rbMyModels = (RadioButton) findViewById(R.id.rbMyModels);
        chkFemale = (CheckBox) findViewById(R.id.chkFemale);
        chkMale = (CheckBox) findViewById(R.id.chkMale);
        chkCouple = (CheckBox) findViewById(R.id.chkCouple);
        chkGroup = (CheckBox) findViewById(R.id.chkGroup);

        // Load saved settings
        loadFilterSettings();

        // Setup search box
        etModelSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveAndApplyFilterSettings();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Setup model filters
        rbAllModels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        rbMyModels.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        // Setup check boxes
        chkFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        chkMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        chkCouple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        chkGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                saveAndApplyFilterSettings();
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // Set version TextView
        ((TextView) findViewById(R.id.tvAppVersion)).setText("v" + Utils.getAppVersion(mContext));

    }

    private void loadFilterSettings() {

        // Load All/My models filter
        String filterSetting = userHandler.getSavedFilterSetting(UserHandler.KEY_SELECTED_MODEL_FILTER);

        if (filterSetting.equals(UserHandler.FILTER_ALL_MODELS) || filterSetting.equals("")) {
            rbAllModels.setChecked(true);
        } else {
            rbMyModels.setChecked(true);
        }

        // Load Female check filter
        filterSetting = userHandler.getSavedFilterSetting(UserHandler.KEY_FILTER_FEMALE);
        chkFemale.setChecked(filterSetting.equals(UserHandler.FILTER_TRUE) || filterSetting.equals(""));

        filterSetting = userHandler.getSavedFilterSetting(UserHandler.KEY_FILTER_MALE);
        chkMale.setChecked(filterSetting.equals(UserHandler.FILTER_TRUE) || filterSetting.equals(""));

        filterSetting = userHandler.getSavedFilterSetting(UserHandler.KEY_FILTER_COUPLE);
        chkCouple.setChecked(filterSetting.equals(UserHandler.FILTER_TRUE) || filterSetting.equals(""));

        filterSetting = userHandler.getSavedFilterSetting(UserHandler.KEY_FILTER_GROUP);
        chkGroup.setChecked(filterSetting.equals(UserHandler.FILTER_TRUE) || filterSetting.equals(""));
    }

    public void saveAndApplyFilterSettings() {

        // Save All/My model filter
        if (rbAllModels.isChecked()) {
            userHandler.saveFilterSetting(UserHandler.KEY_SELECTED_MODEL_FILTER, UserHandler.FILTER_ALL_MODELS);
        } else {
            userHandler.saveFilterSetting(UserHandler.KEY_SELECTED_MODEL_FILTER, UserHandler.FILTER_MY_MODELS);
        }

        // Save Party type filters
        userHandler.saveFilterSetting(UserHandler.KEY_FILTER_FEMALE, chkFemale.isChecked() ? UserHandler.FILTER_TRUE : UserHandler.FILTER_FALSE);
        userHandler.saveFilterSetting(UserHandler.KEY_FILTER_MALE, chkMale.isChecked() ? UserHandler.FILTER_TRUE : UserHandler.FILTER_FALSE);
        userHandler.saveFilterSetting(UserHandler.KEY_FILTER_COUPLE, chkCouple.isChecked() ? UserHandler.FILTER_TRUE : UserHandler.FILTER_FALSE);
        userHandler.saveFilterSetting(UserHandler.KEY_FILTER_GROUP, chkGroup.isChecked() ? UserHandler.FILTER_TRUE : UserHandler.FILTER_FALSE);

        // public void searchModels(String search, boolean allModels, boolean female, boolean male, boolean couple, boolean group)
        allModelsFragment.searchModels(
                etModelSearch.getText().toString(),
                rbAllModels.isChecked(),
                chkFemale.isChecked(),
                chkMale.isChecked(),
                chkCouple.isChecked(),
                chkGroup.isChecked());
    }

    @Override
    public void onModelSelected(int modelId) {
        Bundle bundle = new Bundle();
        bundle.putInt(ModelData.MODEL_ID, modelId);

        modelFragment = ModelFragment.newInstance();
        modelFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.scale_up,
                        R.anim.scale_down)
                .add(R.id.flFragmentHolder, modelFragment)
                .commit();
        findViewById(R.id.flFragmentHolder).setVisibility(View.VISIBLE);

        // Hide filter menu icon while view model
        mMenu.findItem(R.id.action_filter).setVisible(false);

        // Lock menu drawer closed (no filtering while looking at a model)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    @Override
    public void onImageSelected(int modelId, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(ModelData.MODEL_ID, modelId);
        bundle.putInt(ModelData.MODEL_SELECTED_IMAGE_POSITION, position);

        viewModelImagesFragment = ViewModelImagesFragment.newInstance();
        viewModelImagesFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.scale_up,
                        R.anim.scale_down)
                .add(R.id.flFragmentHolder, viewModelImagesFragment)
                .commit();
        findViewById(R.id.flFragmentHolder).setVisibility(View.VISIBLE);

        // Hide action bar
        if (actionBar != null) {
            actionBar.hide();
        }

        // Hide filter menu icon while view model
        mMenu.findItem(R.id.action_filter).setVisible(false);

        // Lock menu drawer closed (no filtering while looking at a model)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void onRegisterClicked() {
        registerFragment = RegisterFragment.newInstance();

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.scale_up,
                        R.anim.scale_down)
                .add(R.id.flFragmentHolder, registerFragment)
                .commit();
        findViewById(R.id.flFragmentHolder).setVisibility(View.VISIBLE);

        // Hide filter menu icon while view model
        mMenu.findItem(R.id.action_filter).setVisible(false);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onBackPressed() {

        if (registerFragment != null && registerFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(registerFragment).commit();

            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else if (viewModelImagesFragment != null && viewModelImagesFragment.isVisible()) {

            modelFragment.setImagePos(viewModelImagesFragment.getImagePos());

            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(viewModelImagesFragment).commit();
            if (actionBar != null) {
                actionBar.show();
            }
        } else if (modelFragment != null && modelFragment.isVisible()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(modelFragment).commit();

            // Show filter icon again
            if (vpFragmentPager.getCurrentItem() == 0) {
                mMenu.findItem(R.id.action_filter).setVisible(true);
            }

            // Unlock menu drawer
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else if (drawerLayout.isDrawerOpen(Gravity.END)) {
            drawerLayout.closeDrawer(Gravity.END);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public void onAnimationComplete() {
        findViewById(R.id.flFragmentHolder).setVisibility(View.GONE);

        // Set menu buttons
        if (vpFragmentPager.getCurrentItem() == 1) {
            mMenu.findItem(R.id.action_filter).setVisible(true);
            mMenu.findItem(R.id.action_add).setVisible(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else if (vpFragmentPager.getCurrentItem() == 2) {
            mMenu.findItem(R.id.action_add).setVisible(true);
            mMenu.findItem(R.id.action_filter).setVisible(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            mMenu.findItem(R.id.action_filter).setVisible(false);
            mMenu.findItem(R.id.action_add).setVisible(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void onRegisterSuccessful() {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(registerFragment).commit();

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        myFragmentAdapter.updateAdapter(0, accountFragment = MyAccountFragment.newInstance());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);

        // Hide add menu button because we only want to add on My Profile view
        mMenu.findItem(R.id.action_add).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_filter:
                if (drawerLayout.isDrawerOpen(Gravity.END)) {
                    drawerLayout.closeDrawer(Gravity.END);
                } else {
                    drawerLayout.openDrawer(Gravity.END);
                }
                return true;

            case R.id.action_add:

                if (profileFragment != null && profileFragment.isVisible()) {
                    profileFragment.showAddNewAlbumDialog();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFilterUpdate(int count) {
        ((TextView) findViewById(R.id.tvFilterCount)).setText(getString(R.string.results_count, count));
    }
}