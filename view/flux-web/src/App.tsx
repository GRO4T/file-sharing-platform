import { useEffect, useState, useRef } from "react";
import FluxAppBar from "./components/FluxAppBar";
import { List } from "@mui/material";
import ListItem from "@mui/material/ListItem";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import ListItemText from "@mui/material/ListItemText";
import ImageIcon from "@mui/icons-material/Image";
import Button from "@mui/material/Button";

const API_URL: string = "http://localhost:8080"

export default function App() {
  const fileInput = useRef<HTMLInputElement>(null);

  const [files, setFiles] = useState<string[]>([]);

  const addFile = async (file: File) => {
    fetch(`${API_URL}/files`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ name: file.name }),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("POST /files failed");
        }
        return response.json();
      })
      .then((data) => {
        uploadFileContent(data.id, data.uploadUrl, file);
      })
      .catch((error) => console.error("Error uploading file: ", error));
  };

  const uploadFileContent = async (fileId: string, uploadUrl: string, file: File) => {
    fetch(uploadUrl, {
      method: "PUT",
      headers: {
        "Content-Type": "application/octet-stream",
      },
      body: file,
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("PUT signed URL failed: " + response.status);
        }
        notifyFileUploaded(fileId);
        fetchFiles();
      })
      .catch((error) =>
        console.error("Cloud Storage upload failed: ", error)
      );
  };

  const notifyFileUploaded = async (fileId: string) => {
    fetch(`${API_URL}/files/${fileId}/upload`, {
      method: "POST",
    })
      .catch((error) => console.error("Error notifying file uploaded: ", error));
  };

  const fetchFiles = async () => {
    fetch(`${API_URL}/files`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("GET /files failed");
        }
        return response.json();
      })
      .then((data) => {
        const names = data.map((item: File) => item.name);
        setFiles(names);
      })
      .catch((error) => console.error("Error fetching files: ", error));
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  useEffect(() => {
    fileInput.current?.addEventListener("change", (event) => {
      const target = event.target as HTMLInputElement;

      if (!target.files || target.files.length == 0) return;

      const file = target.files[0];

      addFile(file);

      target.value = "";
    });
  }, []);

  return (
    <>
      <FluxAppBar />
      <div>
        <Button variant="contained" onClick={() => fileInput.current?.click()}>
          New File
        </Button>
        <input ref={fileInput} type="file" style={{ display: "none" }} />
      </div>
      <List sx={{ width: "100%" }}>
        {files.map((file) => (
          <ListItem
            key={file}
            sx={{
              borderTop: "1px solid #e0e0e0",
              ":hover": { bgcolor: "grey.200" },
            }}
          >
            <ListItemAvatar>
              <Avatar>
                <ImageIcon />
              </Avatar>
            </ListItemAvatar>
            <ListItemText primary={file} secondary="Jan 9, 2014" />
          </ListItem>
        ))}
      </List>
    </>
  );
}
