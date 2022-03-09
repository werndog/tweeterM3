package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.model.service.UserService;

public class RegisterPresenter extends Presenter {

    private UserService userService;

    public RegisterPresenter(View view) {
        super(view);
        userService = new UserService();
    }

    public void registerUser(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        try {
            validateRegistration(firstName, lastName, alias, password, imageToUpload);
            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            // Intentionally, Use the java Base64 encoder so it is compatible with M4.
            String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);
            userService.registerUser(firstName, lastName, alias, password, imageBytesBase64, new GetUserObserver());
        } catch (Exception e) {
            throw e;
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias, String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    @Override
    protected String getMessagePrefix() {
        return "Failed to register";
    }
}
