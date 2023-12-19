package com.example.task4.Fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;

import com.example.task4.DataModels.UserCards;
import com.example.task4.Network.ApiPath;
import com.example.task4.Network.RetrofitClient;
import com.example.task4.R;
import com.google.gson.Gson;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.CardParams;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardInputWidget;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardDialoug extends DialogFragment {
    List<UserCards.Card> cards;
    LinearLayout card_list;
    List<LinearLayout> linearparent = new ArrayList<>();
    List<LinearLayout> linearchild = new ArrayList<>();
    UserCards.Card carddetail;
    private Stripe stripe;
    private CardInputWidget cardInputWidget;
    private String id;
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Stripe with your publishable key
        PaymentConfiguration.init(getContext(), "pk_test_51O9jTFDMkPK75q1NwutmRyOxVaDRkuH5jQPNSKwrguBKFkZ3698Jj7VcuKpig4uQ7ZDxcHaX5HYTK14I0OQH6Ue900fbF04VXT");

        // Create a Stripe instance
        stripe = new Stripe(requireContext(), PaymentConfiguration.getInstance(requireContext()).getPublishableKey());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setupintent, container, false);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);
        Button btnConfirmSetupIntent = view.findViewById(R.id.btn_card);
        card_list = view.findViewById(R.id.card_list);
        getcards();
        // Set up button click listener
        btnConfirmSetupIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmSetupIntent();
            }
        });

        return view;
    }

    private void confirmSetupIntent() {
        // Get PaymentMethodParams from CardInputWidget
        CardParams card = cardInputWidget.getCardParams();
        stripe = new Stripe(requireActivity(), PaymentConfiguration.getInstance(requireActivity()).getPublishableKey());

        stripe.createCardToken(card, new ApiResultCallback<Token>() {
            @Override
            public void onSuccess(@NonNull Token result) {
                String tokenId = result.getId();
                // Send tokenId to your server for further processing

                saveCard(tokenId);
                cardInputWidget.clear();
                Gson gson = new Gson();
                String ob = gson.toJson(result);
            }

            @Override
            public void onError(@NonNull Exception e) {
                // Handle errors
            }
        });
    }

    private void saveCard(String tokenId) {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        UserCards.Id uid = new UserCards.Id();
        uid.setId(id);
        uid.setToken(tokenId);
        Call<UserCards> call = path.setcard(modifiedtoken, uid);
        call.enqueue(new Callback<UserCards>() {
            @Override
            public void onResponse(Call<UserCards> call, Response<UserCards> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    getcards();
                }
            }

            @Override
            public void onFailure(Call<UserCards> call, Throwable t) {

            }
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(requireContext());
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setCanceledOnTouchOutside(false);
        }
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getDialog().getWindow().setLayout(width, height);
        }
    }

    public void setid(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public void getcards() {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        UserCards.Id uid = new UserCards.Id();
        uid.setId(id);
        Call<UserCards> call = path.getcards(modifiedtoken, uid);
        call.enqueue(new Callback<UserCards>() {
            @Override
            public void onResponse(Call<UserCards> call, Response<UserCards> response) {
                if (response.isSuccessful()) {
                    cards = new ArrayList<>();

                    cards = response.body().getCards();
                    showcards();
                }
            }

            @Override
            public void onFailure(Call<UserCards> call, Throwable t) {

            }
        });
    }

    public void deletecard(int index) {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        UserCards.Id uid = new UserCards.Id();
        uid.setUserid(id);
        uid.setCardid(cards.get(index).getId());
        Call<UserCards> call = path.deletcard(modifiedtoken, uid);
        call.enqueue(new Callback<UserCards>() {
            @Override
            public void onResponse(Call<UserCards> call, Response<UserCards> response) {
                if (response.isSuccessful()) {
                    card_list.removeView(linearparent.get(index));
                    Toast.makeText(requireActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    getcards();
                }
            }

            @Override
            public void onFailure(Call<UserCards> call, Throwable t) {

            }
        });
    }

    public void updatecard(int index) {
        String modifiedtoken = "Bearer " + token;
        ApiPath path = RetrofitClient.getRetrofitInstance().create(ApiPath.class);
        UserCards.Id uid = new UserCards.Id();
        uid.setUserid(id);
        uid.setCardid(cards.get(index).getId());
        Call<UserCards> call = path.updatedefaultcard(modifiedtoken, uid);
        call.enqueue(new Callback<UserCards>() {
            @Override
            public void onResponse(Call<UserCards> call, Response<UserCards> response) {
                if (response.isSuccessful())
                {
                    Toast.makeText(requireActivity(),response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    Button btn = new Button(requireActivity());
                    btn.setText("default");
                    btn.setEnabled(false);
                    btn.setHeight(100);
                    btn.setWidth(270);
                    for (int i = 0;i<linearparent.size();i++)
                    {

                       if( linearchild.get(i).getChildCount() == 3)
                       {
                           linearchild.get(i).removeViewAt(2);
                           linearchild.get(index).addView(btn,2);
                       }
                    }


                }
            }

            @Override
            public void onFailure(Call<UserCards> call, Throwable t) {

            }
        });
    }

    private void showcards() {
        card_list.removeAllViews();
        linearparent = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            LinearLayout linearlayoutparent = new LinearLayout(requireActivity());
            linearlayoutparent.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            linearlayoutparent.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout linearLayout = new LinearLayout(requireActivity());
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //     linearLayout.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black));
            linearlayoutparent.addView(linearLayout, 0);
            card_list.addView(linearlayoutparent, i);
            TextView tv = new TextView(requireActivity());

            tv.setText(cards.get(i).getBrand());
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            tv.setLayoutParams(tvParams);

            TextView tv1 = new TextView(requireActivity());

            tv1.setText("  " + "XXXX" + " " + cards.get(i).getLast4());
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv1.setTextColor(Color.GRAY);
            LinearLayout.LayoutParams tvParams1 = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            tv1.setLayoutParams(tvParams1);
            ImageView image = new ImageView(requireActivity());
            image.setImageResource(R.drawable.baseline_more_vert_24);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            imageParams.gravity = Gravity.RIGHT;

            imageParams.setMargins(25, 0, 0, 0); // left, top, right, bottom margins
            image.setLayoutParams(imageParams);

            int[] currentIndex = {i};

            // Set an onClickListener for the TextView


            linearLayout.addView(tv, 0);
            linearLayout.addView(tv1, 1);
            linearlayoutparent.addView(image, 1);
            if (i == 0) {
                Button btn = new Button(requireActivity());
                btn.setText("default");
                btn.setEnabled(false);
                btn.setHeight(100);
                btn.setWidth(270);

                linearLayout.addView(btn, 2);

            }
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = currentIndex[0];
                    showPopupMenu(v, index);
                }
            });
            linearparent.add(linearlayoutparent);
            linearchild.add(linearLayout);
        }
    }

    private void showPopupMenu(View v, int index) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.card, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public int defaultcard = R.id.defaultcard;
            int delete = R.id.delete;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == defaultcard) {
                    updatecard(index);
                }
                if (item.getItemId() == delete) {
                    deletecard(index);

                }
                return true;
            }
        });

        popupMenu.show();
    }
}
