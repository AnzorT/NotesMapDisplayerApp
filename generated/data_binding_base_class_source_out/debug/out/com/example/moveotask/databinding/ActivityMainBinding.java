// Generated by view binder compiler. Do not edit!
package com.example.moveotask.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.moveotask.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText emailEditText;

  @NonNull
  public final TextInputLayout emailTextInputLayout;

  @NonNull
  public final Button loginBtn;

  @NonNull
  public final EditText passwordEditText;

  @NonNull
  public final TextInputLayout passwordTextInputLayout;

  @NonNull
  public final TextView registerTextView;

  @NonNull
  public final Button registrationBtn;

  @NonNull
  public final CheckBox rememberMeCheckBox;

  private ActivityMainBinding(@NonNull LinearLayout rootView, @NonNull EditText emailEditText,
      @NonNull TextInputLayout emailTextInputLayout, @NonNull Button loginBtn,
      @NonNull EditText passwordEditText, @NonNull TextInputLayout passwordTextInputLayout,
      @NonNull TextView registerTextView, @NonNull Button registrationBtn,
      @NonNull CheckBox rememberMeCheckBox) {
    this.rootView = rootView;
    this.emailEditText = emailEditText;
    this.emailTextInputLayout = emailTextInputLayout;
    this.loginBtn = loginBtn;
    this.passwordEditText = passwordEditText;
    this.passwordTextInputLayout = passwordTextInputLayout;
    this.registerTextView = registerTextView;
    this.registrationBtn = registrationBtn;
    this.rememberMeCheckBox = rememberMeCheckBox;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.email_editText;
      EditText emailEditText = ViewBindings.findChildViewById(rootView, id);
      if (emailEditText == null) {
        break missingId;
      }

      id = R.id.email_text_input_layout;
      TextInputLayout emailTextInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (emailTextInputLayout == null) {
        break missingId;
      }

      id = R.id.login_btn;
      Button loginBtn = ViewBindings.findChildViewById(rootView, id);
      if (loginBtn == null) {
        break missingId;
      }

      id = R.id.password_editText;
      EditText passwordEditText = ViewBindings.findChildViewById(rootView, id);
      if (passwordEditText == null) {
        break missingId;
      }

      id = R.id.password_text_input_layout;
      TextInputLayout passwordTextInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (passwordTextInputLayout == null) {
        break missingId;
      }

      id = R.id.register_textView;
      TextView registerTextView = ViewBindings.findChildViewById(rootView, id);
      if (registerTextView == null) {
        break missingId;
      }

      id = R.id.registration_btn;
      Button registrationBtn = ViewBindings.findChildViewById(rootView, id);
      if (registrationBtn == null) {
        break missingId;
      }

      id = R.id.remember_me_checkBox;
      CheckBox rememberMeCheckBox = ViewBindings.findChildViewById(rootView, id);
      if (rememberMeCheckBox == null) {
        break missingId;
      }

      return new ActivityMainBinding((LinearLayout) rootView, emailEditText, emailTextInputLayout,
          loginBtn, passwordEditText, passwordTextInputLayout, registerTextView, registrationBtn,
          rememberMeCheckBox);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
