import { useEffect, useState } from "react";

function useLocalStorageAsState<T>(
  key: string,
  defaultValue?: T
): {
  state: T | null;
  updateLocalStorage: (newValue: T) => void;
  deleteFromLocalStorage: () => void;
} {
  const [state, setState] = useState<T | null>(() => {
    try {
      const storedValue: string | null = localStorage.getItem(key);
      return storedValue ? JSON.parse(storedValue) : defaultValue || null;
    } catch (error) {
      console.log("Unable to parse value from localStorage", error);
      return defaultValue;
    }
  });

  const updateLocalStorage = (newValue: T) => {
    setState(newValue);
  };

  const deleteFromLocalStorage = () => {
    setState(null);
  };

  useEffect(() => {
    if (state !== null) {
      localStorage.setItem(key, JSON.stringify(state));
    } else {
      localStorage.removeItem(key);
    }
  }, [state]);

  return { state, updateLocalStorage, deleteFromLocalStorage };
}

export default useLocalStorageAsState;
