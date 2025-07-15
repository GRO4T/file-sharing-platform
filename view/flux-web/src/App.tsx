import { useEffect, useState, useRef } from "react";
import FluxAppBar from "./components/FluxAppBar";
import { List } from "@mui/material";
import ListItem from "@mui/material/ListItem";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import ListItemText from "@mui/material/ListItemText";
import ImageIcon from "@mui/icons-material/Image";
import Button from "@mui/material/Button";

export default function App() {
  const fileInput = useRef<HTMLInputElement>(null);

  const [files, setFiles] = useState<string[]>([]);

  const fetchFiles = async () => {
    fetch("http://localhost:8080/files")
      .then((response) => {
        if (!response.ok) {
          throw new Error("GET /files failed");
        }
        return response.json();
      })
      .then((data) => {
        const names = data.map((item) => item.name);
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

      fetch("http://localhost:8080/files", {
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
          console.log(data);
          fetch(data.uploadUrl, {
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
              fetchFiles();
            })
            .catch((error) =>
              console.error("Cloud Storage upload failed: ", error)
            );
        })
        .catch((error) => console.error("Error uploading file: ", error));

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
