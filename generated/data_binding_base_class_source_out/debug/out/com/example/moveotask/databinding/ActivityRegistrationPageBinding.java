// Generated by view binder compiler. Do not edit!
package com.example.moveotask.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.moveotask.R;
import com.google.android.material.textfield.TextInputLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegistrationPageBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final EditText emailEditText;

  @NonNull
  public final TextInputLayout emailTextInputLayout;

  @NonNull
  public final EditText firstNameEditText;

  @NonNull
  public final TextInputLayout firstNameTextInputLayout;

  @NonNull
  public final EditText lastNameEditText;

  @NonNull
  public final TextInputLayout lastNameTextInputLayout;

  @NonNull
  public final EditText passwordEditText;

  @NonNull
  public final TextInputLayout passwordTextInputLayout;

  @NonNull
  public final Button registerBtn;

  private ActivityRegistrationPageBinding(@NonNull LinearLayout rootView,
      @NonNull EditText emailEditText, @NonNull TextInputLayout emailTextInputLayout,
      @NonNull EditText firstNameEditText, @NonNull TextInputLayout firstNameTextInputLayout,
      @NonNull EditText lastNameEditText, @NonNull TextInputLayout lastNameTextInputLayout,
      @NonNull EditText passwordEditText, @NonNull TextInputLayout passwordTextInputLayout,
      @NonNull Button registerBtn) {
    this.rootView = rootView;
    this.emailEditText = emailEditText;
    this.emailTextInputLayout = emailTextInputLayout;
    this.firstNameEditText = firstNameEditText;
    this.firstNameTextInputLayout = firstNameTextInputLayout;
    this.lastNameEditText = lastNameEditText;
    this.lastNameTextInputLayout = lastNameTextInputLayout;
    this.passwordEditText = passwordEditText;
    this.passwordTextInputLayout = passwordTextInputLayout;
    this.registerBtn = registerBtn;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegistrationPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegistrationPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_registration_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegistrationPageBinding bind(@NonNull View rootView) {
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

      id = R.id.first_name_editText;
      EditText firstNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (firstNameEditText == null) {
        break missingId;
      }

      id = R.id.first_name_text_input_layout;
      TextInputLayout firstNameTextInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (firstNameTextInputLayout == null) {
        break missingId;
      }

      id = R.id.last_name_editText;
      EditText lastNameEditText = ViewBindings.findChildViewById(rootView, id);
      if (lastNameEditText == null) {
        break missingId;
      }

      id = R.id.last_name_text_input_layout;
      TextInputLayout lastNameTextInputLayout = ViewBindings.findChildViewById(rootView, id);
      if (lastNameTextInputLayout == null) {
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

      id = R.id.register_btn;
      Button registerBtn = ViewBindings.findChildViewById(rootView, id);
      if (registerBtn == null) {
        break missingId;
      }

      return new ActivityRegistrationPageBinding((LinearLayout) rootView, emailEditText,
          emailTextInputLayout, firstNameEditText, firstNameTextInputLayout, lastNameEditText,
          lastNameTextInputLayout, passwordEditText, passwordTextInputLayout, registerBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
