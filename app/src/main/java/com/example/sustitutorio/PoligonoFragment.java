package com.example.sustitutorio;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Fragmento que muestra un polígono utilizando un CanvasView.
 */
public class PoligonoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PoligonoFragment() {
        // Constructor público requerido por Fragment.
    }

    /**
     * Método de fábrica para crear una nueva instancia de este fragmento.
     *
     * @param param1 Parámetro 1.
     * @param param2 Parámetro 2.
     * @return Una nueva instancia de fragmento PoligonoFragment.
     */
    public static PoligonoFragment newInstance(String param1, String param2) {
        PoligonoFragment fragment = new PoligonoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el diseño del fragmento
        View view = inflater.inflate(R.layout.fragment_poligono, container, false);

        // Obtener el contenedor del lienzo desde el diseño
        FrameLayout frameLayout = view.findViewById(R.id.canvas_container);

        // Crear una instancia de CanvasView y agregarlo al contenedor
        CanvasView canvasView = new CanvasView(getContext());
        frameLayout.addView(canvasView);

        return view;
    }
}
