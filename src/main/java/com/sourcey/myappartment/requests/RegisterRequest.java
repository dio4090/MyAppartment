package com.sourcey.myappartment.requests;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by diogo on 27/11/2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://br276.teste.website/~delle375/register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String address, String email, int mobile_number, String password,Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("address", address);
        params.put("email", email);
        params.put("mobile_number", Integer.toString(mobile_number));
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
